package com.ssafy.gallery.image.repository;

import com.ssafy.gallery.image.model.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepository {
    private final ImageJpaRepository imageJpaRepository;

    public ImageInfo insertImageUrls(ImageInfo imageInfo) {
        return imageJpaRepository.save(imageInfo);
    }

    public List<ImageInfo> getImageInfoListByUserId(int userId) {
        return imageJpaRepository.findImageInfoByUserId(userId);
    }

    public void deleteImageInfo(int imageInfoId) {
        imageJpaRepository.findById(imageInfoId)
                .map(imageInfo -> {
                    imageInfo.markAsDeleted();
                    return imageInfo;
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지 정보가 존재하지 않습니다. imageInfoId=" + imageInfoId));

        imageJpaRepository.flush();
    }

    public Resource getImageByImageInfoId(int imageInfoId) {

        return null;
    }
}
