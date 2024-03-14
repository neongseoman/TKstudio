package com.ssafy.gallery.image.service;

import com.ssafy.gallery.common.exception.ApiExceptionFactory;
import com.ssafy.gallery.image.exception.ImageExceptionEnum;
import com.ssafy.gallery.image.model.ImageInfo;
import com.ssafy.pjt.grpc.*;
import com.google.protobuf.ByteString;
import com.ssafy.gallery.image.model.ImageOption;
import com.ssafy.pjt.grpc.CreateImageGrpc;
import com.ssafy.pjt.grpc.Image;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Log4j2
@Service
public class ImageService {
    private final String aiUrl = System.getenv("AI_URL");
    private final ManagedChannel channel
            = ManagedChannelBuilder.forTarget(aiUrl).usePlaintext().build();

    private final CreateImageGrpc.CreateImageBlockingStub imageStub
            = CreateImageGrpc.newBlockingStub(channel);

    public ByteArrayResource sendImage(MultipartFile image, ImageOption imageOption) throws IOException {
        ByteString imageData = ByteString.copyFrom(image.getBytes());
        Image.Options options = Image.Options.newBuilder()
                .setBackground(imageOption.getBackground())
                .setHair(imageOption.getHair())
                .setSuit(imageOption.getSuit()).build();

        try {
            receiveData = this.imageStub.sendImage(Image.OriginalImageInfo.newBuilder()
                    .setOriginalImage(imageData)
                    .setOptions(options)
                    .build());
        } catch (IllegalStateException e) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.GRPC_ERROR);
        }

        ByteArrayResource byteArrayResource = new ByteArrayResource(receiveData.getProcessedImage().toByteArray());

//        System.out.println("받은 파일 bytes 크기 : " + byteArrayResource.contentLength());
            return byteArrayResource;
        } else if (Image.ImageProcessingResult.NO_FACE.equals(receiveData.getResult())) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.NO_FACE);
        } else if (Image.ImageProcessingResult.MANY_FACE.equals(receiveData.getResult())) {
            throw ApiExceptionFactory.fromExceptionEnum(ImageExceptionEnum.MANY_FACE);
        } else {
            throw new Exception("UNKOWN ERROR");
        }
    }
}
