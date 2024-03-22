package com.ssafy.gallery.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.google.protobuf.ByteString;
import com.ssafy.gallery.common.exception.ApiExceptionFactory;
import com.ssafy.gallery.common.stub.GrpcStubPool;
import com.ssafy.gallery.image.exception.ImageExceptionEnum;
import com.ssafy.gallery.image.model.*;
import com.ssafy.gallery.image.repository.ImageRedisRepository;
import com.ssafy.gallery.image.repository.ImageRepository;
import com.ssafy.pjt.grpc.CreateImageGrpc;
import com.ssafy.pjt.grpc.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageService {
    private ResourceLoader resourceLoader;
    private final String aiUrl = System.getenv("AI_URL");
    private final ImageRepository imageRepository;
    private final ImageRedisRepository imageRedisRepository;
    private final GrpcStubPool grpcStubPool;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public CreateImageDto createImage(MultipartFile image, ImageOption imageOption, int userId) throws Exception {
        ByteString imageData = ByteString.copyFrom(image.getBytes());
        CreateImageGrpc.CreateImageBlockingStub imageStub = null;

        Image.ProcessedImageInfo receiveData = null;
        Image.Options options = Image.Options.newBuilder()
                .setBackground(imageOption.getBackground())
                .setHair(imageOption.getHair())
                .setSex(imageOption.getSex())
                .setSuit(imageOption.getSuit()).build();
        try {
            imageStub = grpcStubPool.getStub();
            receiveData = imageStub.sendImage(Image.OriginalImageInfo.newBuilder()
                    .setOriginalImage(imageData)
                    .setOptions(options)
                    .build());
        } catch (IllegalStateException e) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.GRPC_ERROR);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to get gRPC stub from the pool", e);
        } finally {
            if (imageStub != null) {
                grpcStubPool.returnStub(imageStub);
            }
        }


        if (Image.ImageProcessingResult.SUCCESS.equals(receiveData.getResult())) {
            byte[] processedImageData = receiveData.getProcessedImage().toByteArray();
            ByteArrayResource byteArrayResource = getBufferedImage(processedImageData);
            Image.ResponseUrl responseUrl = receiveData.getResponseUrl();

            ImageInfo imageInfo = new ImageInfo(
                    userId,
                    responseUrl.getOriginalImageUrl(),
                    responseUrl.getThumbnailImageUrl(),
                    responseUrl.getProcessedImageUrl());

            ImageInfo insertResult = imageRepository.insertImageUrls(imageInfo);
            log.info("DB insert Image info : " + insertResult.getImageInfoId());

            CreateImageDto imageInfoDto = new CreateImageDto(
                    imageInfo.getImageInfoId(),
                    imageInfo.getThumbnailImageUrl(),
                    imageInfo.getOriginalImageUrl(),
                    imageInfo.getProcessedImageUrl(),
                    byteArrayResource
            );

            return imageInfoDto;
        } else if (Image.ImageProcessingResult.NO_FACE.equals(receiveData.getResult())) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.NO_FACE);
        } else if (Image.ImageProcessingResult.MANY_FACE.equals(receiveData.getResult())) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.MANY_FACE);
        } else {
            throw new Exception("UNKOWN ERROR");
        }
    }

    public List<ImageInfoDTO> getImageInfos(int userId) {
        List<ImageInfo> imageInfoList = imageRepository.getImageInfoListByUserId(userId);
        List<ImageInfoRedisDTO> redisImageInfoList = imageInfoList.stream()
                .map(ImageInfoRedisDTO::new)
                .toList();

        imageRedisRepository.saveAll(redisImageInfoList);
        List<ImageInfoDTO> listDto = imageInfoList.stream()
                .map(ImageInfoDTO::new)
                .toList();
        return listDto;
    }

    public Resource getOrigianlImage(String imageInfoId) throws Exception {
        System.out.println(imageInfoId);

        ImageInfoRedisDTO imageInfo = Optional.ofNullable(imageRedisRepository.findById(imageInfoId)
                .orElseThrow(() -> new Exception("Redis error"))).get();
        String originalImageURL = imageInfo.getOriginalImageUrl();


        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket,originalImageURL));
        S3ObjectInputStream objectInputStream = object.getObjectContent();
        byte[] data = IOUtils.toByteArray(objectInputStream);
        ByteArrayResource resource = new ByteArrayResource(data);

        return resource;
    }

    public Resource getProcessedImage(String imageInfoId) throws Exception {
        Optional<ImageInfoRedisDTO> imageInfo = Optional.ofNullable(imageRedisRepository.findById(imageInfoId)
                .orElseThrow( () -> new Exception("redis infomation error")));

        String processedImageURL = imageInfo.get().getProcessedImageUrl();
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket, processedImageURL));
        S3ObjectInputStream objectInputStream = object.getObjectContent();
        byte[] data = IOUtils.toByteArray(objectInputStream);
        ByteArrayResource resource = new ByteArrayResource(data);

        return resource;
    }

    public Resource getThumbnailImage(String imageInfoId) throws Exception {
        Optional<ImageInfoRedisDTO> imageInfo = Optional.ofNullable(imageRedisRepository.findById(imageInfoId)
                .orElseThrow( () -> new Exception("redis infomation error")));

        String thumbnailImageURL = imageInfo.get().getThumbnailImageUrl();
        S3Object object = amazonS3.getObject(new GetObjectRequest(bucket, thumbnailImageURL));
        S3ObjectInputStream objectInputStream = object.getObjectContent();
        byte[] data = IOUtils.toByteArray(objectInputStream);
        ByteArrayResource resource = new ByteArrayResource(data);

        return resource;
    }
    public void deleteImage(int imageId) {
        imageRepository.deleteImageInfo(imageId);
    }


    private static ByteArrayResource getBufferedImage(byte[] processedImageData) throws IOException, IOException {
        BufferedImage bufferedImage = new BufferedImage(768, 1024, BufferedImage.TYPE_3BYTE_BGR);

        // BufferedImage에 byte 배열 데이터 채우기
        int index = 0;
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int red = processedImageData[index++] & 0xFF;
                int green = processedImageData[index++] & 0xFF;
                int blue = processedImageData[index++] & 0xFF;

                // RGB 값으로 Pixel 생성 및 설정
                bufferedImage.setRGB(x, y, new Color(blue, green, red).getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);

        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        return resource;
    }

}
