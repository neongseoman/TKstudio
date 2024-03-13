package com.ssafy.gallery.image.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageInfoId;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false, length = 200)
    private String thumbnailImageUrl;

    @Column(nullable = false, length = 200)
    private String originalImageUrl;

    @Column(nullable = false, length = 200)
    private String processedImageUrl;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private LocalDateTime createdTime;

//    @OneToMany(mappedBy = "selectOptionId")
//    private List<SelectOption> selectOptions = new ArrayList<>();
//
//    public void addSelectOption(SelectOption selectOption){
//        this.selectOptions.add(selectOption);
//    }

    // Getters and setters omitted for brevity
}
