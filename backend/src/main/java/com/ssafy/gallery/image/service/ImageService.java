package com.ssafy.gallery.image.service;

import com.google.protobuf.ByteString;
import com.ssafy.gallery.common.exception.ApiExceptionFactory;
import com.ssafy.gallery.image.exception.ImageExceptionEnum;
import com.ssafy.gallery.image.model.CreateImageDto;
import com.ssafy.gallery.image.model.ImageInfo;
import com.ssafy.gallery.image.model.ImageOption;
import com.ssafy.gallery.image.repository.ImageRepository;
import com.ssafy.pjt.grpc.CreateImageGrpc;
import com.ssafy.pjt.grpc.Image;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageService {
    private ResourceLoader resourceLoader;
    private final String aiUrl = System.getenv("AI_URL");
    private ImageRepository imageRepository;

    private final ManagedChannel channel
            = ManagedChannelBuilder.forTarget(aiUrl).usePlaintext().build();

    private final CreateImageGrpc.CreateImageBlockingStub imageStub
            = CreateImageGrpc.newBlockingStub(channel);

    public CreateImageDto sendImage(MultipartFile image, ImageOption imageOption) throws Exception {
        ByteString imageData = ByteString.copyFrom(image.getBytes());

        Image.ProcessedImageInfo receiveData = null;
        Image.Options options = Image.Options.newBuilder()
                .setBackground(imageOption.getBackground())
                .setHair(imageOption.getHair())
                .setSex(imageOption.getSex())
                .setSuit(imageOption.getSuit()).build();

        try {
            receiveData = this.imageStub.sendImage(Image.OriginalImageInfo.newBuilder()
                    .setOriginalImage(imageData)
                    .setOptions(options)
                    .build());
        } catch (IllegalStateException e) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.GRPC_ERROR);
        }

        if (Image.ImageProcessingResult.SUCCESS.equals(receiveData.getResult())) {
            byte[] processedImageData = receiveData.getProcessedImage().toByteArray();

            Image.ResponseUrl responseUrl = receiveData.getResponseUrl();

            ImageInfo imageInfo = imageRepository.insertImageUrls(new ImageInfo(
                    1, // 나중에 UserId로 수정해야함.
                    responseUrl.getThumbnailImageUrl(),
                    responseUrl.getOriginalImageUrl(),
                    responseUrl.getProcessedImageUrl()
            ));

            CreateImageDto imageInfoDto = new CreateImageDto(
                    imageInfo.getImageInfoId(),
                    responseUrl.getThumbnailImageUrl(),
                    responseUrl.getOriginalImageUrl(),
                    responseUrl.getProcessedImageUrl());

            return imageInfoDto;
        } else if (Image.ImageProcessingResult.NO_FACE.equals(receiveData.getResult())) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.NO_FACE);
        } else if (Image.ImageProcessingResult.MANY_FACE.equals(receiveData.getResult())) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.MANY_FACE);
        } else {
            throw new Exception("UNKOWN ERROR");
        }
    }

    public ImageInfo getImage(int imageId){
        return imageRepository.getImage(imageId);
    }

    public List<ImageInfo> getImages(int userId){
        return imageRepository.getImageInfoListByUserId(userId);
    }

    public void deleteImage(int imageId){
        imageRepository.deleteImageInfo(imageId);
    }
}
