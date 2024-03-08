package com.ssafy.gallery.image.service;

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
    private final ManagedChannel channel
            = ManagedChannelBuilder.forTarget("localhost:9090").usePlaintext().build();
    private final CreateImageGrpc.CreateImageBlockingStub imageStub
            = CreateImageGrpc.newBlockingStub(channel);

    public ByteArrayResource sendImage(MultipartFile image, ImageOption imageOption) throws IOException {
        ByteString imageData = ByteString.copyFrom(image.getBytes());
        Image.Options options = Image.Options.newBuilder()
                .setBackground(imageOption.getBackground())
                .setHair(imageOption.getHair())
                .setSuit(imageOption.getSuit()).build();

        Image.ProcessedImageInfo receiveData =
                this.imageStub.sendImage(Image.OriginalImageInfo.newBuilder()
                        .setOriginalImage(imageData)
                        .setOptions(options)
                        .build());
        ByteArrayResource byteArrayResource = new ByteArrayResource(receiveData.getProcessedImage().toByteArray());

        System.out.println("받은 파일 bytes 크기 : " + byteArrayResource.contentLength());
        return byteArrayResource;
    }
}
