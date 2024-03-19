package com.ssafy.gallery.image.repository;

import com.ssafy.gallery.image.model.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageInfoRepository extends JpaRepository<ImageInfo, Integer> {
    List<ImageInfo> findByUserId(int userId);
}
