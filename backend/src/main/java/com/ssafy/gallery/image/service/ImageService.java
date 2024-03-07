package com.ssafy.gallery.image.service;

import com.google.protobuf.ByteString;
import com.ssafy.gallery.image.model.ImageOption;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chb.examples.lib.CreateImageGrpc;
import org.chb.examples.lib.Image;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Log4j2
@Service
public class ImageService {
    private static final Logger logger = LogManager.getLogger(ImageService.class);

    ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:9090").usePlaintext().build();

    CreateImageGrpc.CreateImageBlockingStub imageStub = CreateImageGrpc.newBlockingStub(channel);

    public Resource sendImage(MultipartFile image) throws IOException {
        System.out.println("원본 파일 bytes 크기" + image.getBytes().length);
        ByteString imageData = ByteString.copyFrom(image.getBytes());
        System.out.println("보낼 때 bytes 크기" + image.getBytes().length);
        Image.processedImage receiveData = this.imageStub.sendImage(Image.originalImage.newBuilder().setImage(imageData).setName("TestImage").build());
        ByteArrayResource byteArrayResource = new ByteArrayResource(receiveData.getImage().toByteArray());
        ByteArrayInputStream bis = new ByteArrayInputStream(receiveData.getImage().toByteArray());
        ImageIO.read(bis);


        System.out.println("받은 파일 bytes 크기" + byteArrayResource.contentLength());

        return byteArrayResource;
    }
}
