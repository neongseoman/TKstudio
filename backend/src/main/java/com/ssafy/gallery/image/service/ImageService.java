package com.ssafy.gallery.image.service;

import com.google.protobuf.ByteString;
import com.ssafy.gallery.image.model.ImageInfo;
import com.ssafy.gallery.image.model.ImageOption;
import com.ssafy.pjt.grpc.CreateImageGrpc;
import com.ssafy.pjt.grpc.Image;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Log4j2
@Service
public class ImageService {

    @Autowired
    private ResourceLoader resourceLoader;

    private final ManagedChannel channel
            = ManagedChannelBuilder.forTarget("localhost:9090").usePlaintext().build();

    private final CreateImageGrpc.CreateImageBlockingStub imageStub
            = CreateImageGrpc.newBlockingStub(channel);

    public ByteArrayResource sendImage(MultipartFile image, ImageOption imageOption) throws IOException {
        ByteString imageData = ByteString.copyFrom(image.getBytes());
        BufferedImage bufferedImage = null;
        Image.Options options = Image.Options.newBuilder()
                .setBackground(imageOption.getBackground())
                .setHair(imageOption.getHair())
                .setSuit(imageOption.getSuit()).build();

        Image.ProcessedImageInfo receiveData =
                this.imageStub.sendImage(Image.OriginalImageInfo.newBuilder()
                        .setOriginalImage(imageData)
                        .setOptions(options)
                        .build());

        byte[] processedImageData = receiveData.getProcessedImage().toByteArray();
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(processedImageData);
//        ByteArrayOutputStream outStreamObj = new ByteArrayOutputStream();
//        BufferedImage bufferedImage = ImageIO.read(inputStream);

//        ImageIO.write(bufferedImage, "png", outStreamObj); //저장하고자 하는 파일 경로를 입력합니다.
        bufferedImage = new BufferedImage(354,472,BufferedImage.TYPE_3BYTE_BGR);

// BufferedImage에 byte 배열 데이터 채우기
        int index = 0;
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int red = processedImageData[index++] & 0xFF;
                int green = processedImageData[index++] & 0xFF;
                int blue = processedImageData[index++] & 0xFF;

                // RGB 값으로 Pixel 생성 및 설정
                bufferedImage.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        ByteArrayResource byteArrayResource= new ByteArrayResource( baos.toByteArray());
        Image.ResponseUrl responseUrl = receiveData.getResponseUrl();
        ImageInfo imageInfo = new ImageInfo(1,responseUrl.getThumbnailImageUrl(),
                responseUrl.getOriginalImageUrl(),
                responseUrl.getProcessedImageUrl());
//        System.out.println("받은 파일 bytes 크기 : " + byteArrayResource.contentLength());
        return byteArrayResource;
    }
}
