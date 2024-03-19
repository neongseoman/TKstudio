// ImageJpaRepository.java
package com.ssafy.gallery.image.repository;

import com.ssafy.gallery.image.model.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageJpaRepository extends JpaRepository<ImageInfo, Integer> {
    @Query("SELECT i FROM ImageInfo i WHERE i.userId = :userId AND i.isDeleted = false")
    List<ImageInfo> findImageInfoByUserId(@Param("userId") int userId);
}