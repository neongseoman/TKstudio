package com.ssafy.gallery.image.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageInfoId;

    @Column(nullable = false)
    private int userId;

    @Column()
    private String originalImageUrl;

    @Column()
    private String thumbnailImageUrl;

    @Column()
    private String processedImageUrl;

    @Column()
    private boolean isDeleted = false;

    @CreatedDate
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "imageInfo", fetch = FetchType.LAZY)
    private List<SelectOption> selectOptions = new ArrayList<>();

    public ImageInfo(int userId, String originalImageUrl, String thumbnailImageUrl, String processedImageUrl) {
        this.userId = userId;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.originalImageUrl = originalImageUrl;
        this.processedImageUrl = processedImageUrl;
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