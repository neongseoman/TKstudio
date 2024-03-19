package com.ssafy.gallery.image.repository;

import com.ssafy.gallery.image.model.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageRepository {
    private ImageInfoRepository imageInfoRepository;

    public ImageInfo insertImageUrls(ImageInfo imageInfo) {
        return imageInfoRepository.save(imageInfo);
    }

    @Transactional(readOnly = true)
    public List<ImageInfo> getImageInfoListByUserId(int userId) {
        return imageInfoRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteImageInfo(int imageInfoId) {
        ImageInfo imageInfo = imageInfoRepository.findById(imageInfoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지 정보가 존재하지 않습니다. imageInfoId=" + imageInfoId));

        imageInfo.markAsDeleted();
        imageInfoRepository.save(imageInfo);
    }

    @Transactional
    public ImageInfo getImage(int imageId) {
        return imageInfoRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이미지 정보가 존재하지 않습니다. imageInfoId="+imageId));

    }
}
