package com.ssafy.gallery.image.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "imageInfo", fetch = FetchType.LAZY)
    private List<SelectOption> selectOptions = new ArrayList<>();

    public ImageInfo(int i, String thumbnailImageUrl, String originalImageUrl, String processedImageUrl) {
    }

    public void addSelectOption(SelectOption selectOption) {
        selectOptions.add(selectOption);
    }

    public void removeSelectOption(SelectOption selectOption) {
        selectOptions.remove(selectOption);
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}