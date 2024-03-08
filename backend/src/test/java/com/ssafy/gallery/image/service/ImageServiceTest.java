package com.ssafy.gallery.image.service;

import com.google.protobuf.ByteString;
import com.ssafy.gallery.image.model.ImageOption;
import com.ssafy.pjt.grpc.CreateImageGrpc;
import com.ssafy.pjt.grpc.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private CreateImageGrpc.CreateImageBlockingStub imageStub;

    @InjectMocks
    private ImageService imageService;


    @Test
    @DisplayName("gRPC send image ")
    void sendImage() throws IOException {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "originalImage",
                "test.jpg",
                "image/jpeg",
                "This is a test image".getBytes()
        );

        // 2. 가짜 ImageOption 객체 생성
        ImageOption imageOption = new ImageOption("background1", "suit1", "hair1");

        // 3. 가짜 데이터 생성
        ByteString imageData = ByteString.copyFrom(mockMultipartFile.getBytes());
        Image.Options options = Image.Options.newBuilder()
                .setBackground(imageOption.getBackground())
                .setHair(imageOption.getHair())
                .setSuit(imageOption.getSuit()).build();

        Image.OriginalImageInfo originalImageInfo = Image.OriginalImageInfo.newBuilder()
                .setOriginalImage(imageData)
                .setOptions(options)
                .build();

        // 4. mock 설정 (imageStub.sendImage)
        Image.ProcessedImageInfo processedImageInfo = Image.ProcessedImageInfo.newBuilder()
                .setProcessedImage(ByteString.copyFromUtf8("processed image data"))
                .build();

        Mockito.lenient().when(imageStub.sendImage(originalImageInfo)).thenReturn(processedImageInfo);

        // 5. 테스트 실행
        ByteArrayResource response = imageService.sendImage(mockMultipartFile, imageOption);

        // 6. 검증
        assertEquals(processedImageInfo.getProcessedImage().toByteArray().length, response.contentLength());
        System.out.println("받은 파일 bytes 크기 : " + response.contentLength());

    }
}