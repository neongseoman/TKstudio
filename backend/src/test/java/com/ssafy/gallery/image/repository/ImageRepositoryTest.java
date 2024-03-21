package com.ssafy.gallery.image.repository;

import com.ssafy.gallery.image.model.ImageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ImageRepositoryTest {

    @InjectMocks
    private ImageRepository imageRepository;
    // @InjectMocks는 테스트 대상 클래스인 ImageRepository의 인스턴스를 생성하고,
    // 해당 인스턴스의 의존성을 @Mock으로 선언된 객체로 주입합니다.

    @Mock
    private ImageJpaRepository imageJpaRepository;
    // @Mock은 ImageJpaRepository의 가짜(모의) 객체를 생성합니다.
    // 이 객체는 실제 구현을 가지고 있지 않으며, 테스트 목적으로 사용됩니다.

    private ImageInfo imageInfo;

    @BeforeEach
    @DisplayName("ImageInfo init")
    void setup() {
        imageInfo = ImageInfo.builder()
                .userId(1)
                .originalImageUrl("original_url")
                .thumbnailImageUrl("thumbnail_url")
                .processedImageUrl("processed_url")
                .build();
        // @BeforeEach는 각 테스트 메서드 실행 전에 수행되는 설정 메서드입니다.
        // 여기서는 테스트에 사용될 ImageInfo 객체를 생성합니다.
    }

    @Test
    @DisplayName("insertImageUrls_ShouldSaveImageInfo")
    void insertImageUrls_ShouldSaveImageInfo() {
        // given
        when(imageJpaRepository.save(imageInfo)).thenReturn(imageInfo);
        // Mockito의 when을 사용하여 imageJpaRepository의 save 메서드가 호출될 때,
        // any(ImageInfo.class)로 전달된 인자와 상관없이 미리 정의한 imageInfo 객체를 반환하도록 설정합니다.

        // when
        ImageInfo result = imageRepository.insertImageUrls(imageInfo);
        // imageRepository의 insertImageUrls 메서드를 호출하여 결과를 result 변수에 저장합니다.

        // then
        assertEquals(imageInfo, result);
        // assertEquals를 사용하여 결과(result)와 예상 값(imageInfo)이 일치하는지 검증합니다.
        verify(imageJpaRepository, times(1)).save(imageInfo);
        // verify를 사용하여 imageJpaRepository의 save 메서드가 imageInfo 객체와 함께 한 번 호출되었는지 검증합니다.
    }

    @Test
    @DisplayName("getImageInfoListByUserId_ShouldReturnImageInfoList")
    void getImageInfoListByUserId_ShouldReturnImageInfoList() {
        // given
        List<ImageInfo> imageInfoList = new ArrayList<>();
        imageInfoList.add(imageInfo);
        when(imageJpaRepository.findImageInfoByUserId(1)).thenReturn(imageInfoList);
        // Mockito의 when을 사용하여 imageJpaRepository의 findImageInfoByUserId 메서드가
        // 인자 1과 함께 호출될 때, 미리 정의한 imageInfoList를 반환하도록 설정합니다.

        // when
        List<ImageInfo> result = imageRepository.getImageInfoListByUserId(1);
        // imageRepository의 getImageInfoListByUserId 메서드를 호출하여 결과를 result 변수에 저장합니다.

        // then
        assertEquals(imageInfoList, result);
        // assertEquals를 사용하여 결과(result)와 예상 값(imageInfoList)이 일치하는지 검증합니다.
        verify(imageJpaRepository, times(1)).findImageInfoByUserId(1);
        // verify를 사용하여 imageJpaRepository의 findImageInfoByUserId 메서드가 인자 1과 함께 한 번 호출되었는지 검증합니다.
    }

    @Test
    @DisplayName("deleteImageInfo_ShouldMarkImageInfoAsDeleted")
    void deleteImageInfo_ShouldMarkImageInfoAsDeleted() {
        // given
        when(imageJpaRepository.findById(1)).thenReturn(Optional.of(imageInfo));
        // Mockito의 when을 사용하여 imageJpaRepository의 findById 메서드가
        // 인자 1과 함께 호출될 때, 미리 정의한 imageInfo 객체를 Optional로 감싸서 반환하도록 설정합니다.

        // when
        imageRepository.deleteImageInfo(1);
        // imageRepository의 deleteImageInfo 메서드를 호출합니다.

        // then
        assertTrue(imageInfo.isDeleted());
        // assertTrue를 사용하여 imageInfo의 isDeleted 필드가 true인지 검증합니다.
        verify(imageJpaRepository, times(1)).findById(1);
        // verify를 사용하여 imageJpaRepository의 findById 메서드가 인자 1과 함께 한 번 호출되었는지 검증합니다.
        verify(imageJpaRepository, times(1)).flush();
        // verify를 사용하여 imageJpaRepository의 flush 메서드가 한 번 호출되었는지 검증합니다.
    }

    @Test
    @DisplayName("deleteImageInfo_ShouldThrowExceptionWhenImageInfoNotFound")
    void deleteImageInfo_ShouldThrowExceptionWhenImageInfoNotFound() {
        // given
        when(imageJpaRepository.findById(1)).thenReturn(Optional.empty());
        // Mockito의 when을 사용하여 imageJpaRepository의 findById 메서드가
        // 인자 1과 함께 호출될 때, 빈 Optional을 반환하도록 설정합니다.

        // when, then
        assertThrows(IllegalArgumentException.class, () -> imageRepository.deleteImageInfo(1));
        // assertThrows를 사용하여 imageRepository의 deleteImageInfo 메서드를 호출할 때,
        // IllegalArgumentException이 발생하는지 검증합니다.
        verify(imageJpaRepository, times(1)).findById(1);
        // verify를 사용하여 imageJpaRepository의 findById 메서드가 인자 1과 함께 한 번 호출되었는지 검증합니다.
        verify(imageJpaRepository, never()).flush();
        // verify를 사용하여 imageJpaRepository의 flush 메서드가 호출되지 않았는지 검증합니다.
    }

    @Test
    void getImageByImageInfoId_ShouldReturnResource() {
        // TODO: Implement test case when getImageByImageInfoId method is implemented
        // getImageByImageInfoId 메서드가 구현되면 해당 테스트 케이스를 작성합니다.
    }
}