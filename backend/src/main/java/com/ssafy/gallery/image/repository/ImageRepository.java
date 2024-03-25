package com.ssafy.gallery.image.repository;

import com.ssafy.gallery.image.model.ImageInfo;
import com.ssafy.gallery.image.model.ImageInfoDTO;
import com.ssafy.gallery.image.model.SelectOptionDTO;
import com.ssafy.pjt.grpc.Image;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ImageRepository {
    @PersistenceContext
    private EntityManager em;

    private final ImageJpaRepository imageJpaRepository;

    public ImageInfo insertImageUrls(ImageInfo imageInfo) {
        return imageJpaRepository.save(imageInfo);
    }

    public List<ImageInfo> getImageInfoListByUserId(int userId) {
        String jpql = "SELECT i FROM ImageInfo i LEFT JOIN FETCH i.selectOptions WHERE i.userId = :userId AND i.isDeleted = false";
        TypedQuery<ImageInfo> query = em.createQuery(jpql,ImageInfo.class);
        query.setParameter("userId", userId);
        List<ImageInfo> list = query.getResultList();
        return list;
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

}
