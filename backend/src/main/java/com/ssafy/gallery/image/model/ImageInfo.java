package com.ssafy.gallery.image.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ImageInfo {

    public ImageInfo(int userId, String thumbnailImageUrl, String originalImageUrl, String processedImageUrl) {
        this.userId = userId;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.originalImageUrl = originalImageUrl;
        this.processedImageUrl = processedImageUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_INFO_ID")
    private int imageInfoId;

    @Column(name = "USER_ID", nullable = false)
    private int userId;

    @Column(name = "THUMBNAIL_IMAGE_URL", nullable = false, length = 200)
    private String thumbnailImageUrl;

    @Column(name = "ORIGINAL_IMAGE_URL", nullable = false, length = 200)
    private String originalImageUrl;

    @Column(name = "PROCESSED_IMAGE_URL", nullable = false, length = 200)
    private String processedImageUrl;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean isDeleted = false;

    @CreatedDate
    @Column(name = "CREATED_TIME", nullable = false)
    private LocalDateTime createdTime;

    public ImageInfo() {

    }

    @OneToMany(mappedBy = "imageInfo", orphanRemoval = true)
    private List<SelectOption> selectOptions = new ArrayList<>();

    public void addSelectOption(SelectOption selectOption){
        this.selectOptions.add(selectOption);
//        selectOption.setImageInfo(this);
    }

    public void removeSelectOption(SelectOption selectOption){
        this.selectOptions.remove(selectOption);
//        selectOption.setImageInfo(null);
    }
}