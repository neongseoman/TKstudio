package com.ssafy.gallery.image.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageInfoId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String originalImageUrl;

    @Column(nullable = false)
    private String thumbnailImageUrl;

    @Column(nullable = false)
    private String processedImageUrl;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public ImageInfo(int userId, String originalImageUrl, String thumbnailImageUrl, String processedImageUrl) {
        this.userId = userId;
        this.thumbnailImageUrl = thumbnailImageUrl.substring(thumbnailImageUrl.indexOf("thumbnailImages/"));
        this.originalImageUrl = originalImageUrl.substring(originalImageUrl.indexOf("originalImages/"));
        this.processedImageUrl = processedImageUrl.substring(processedImageUrl.indexOf("processedImages/"));
    }

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "imageInfo")
    private List<SelectOption> selectOptions = new ArrayList<>();

    public void addSelectOption(SelectOption selectOption) {
        selectOptions.add(selectOption);
    }

    public void markAsDeleted() {
        isDeleted = true;
    }
}