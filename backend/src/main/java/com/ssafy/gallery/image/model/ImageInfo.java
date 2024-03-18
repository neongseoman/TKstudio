package com.ssafy.gallery.image.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
public class ImageInfo extends CreateImage {

    public ImageInfo(int userId, String thumbnailImageUrl, String originalImageUrl, String processedImageUrl) {
        super(thumbnailImageUrl,originalImageUrl,processedImageUrl);
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageInfoId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdTime;

    public ImageInfo() {

    }

//    @OneToMany(mappedBy = "selectOptionId")
//    private List<SelectOption> selectOptions = new ArrayList<>();
//
//    public void addSelectOption(SelectOption selectOption){
//        this.selectOptions.add(selectOption);
//    }

    // Getters and setters omitted for brevity
}
