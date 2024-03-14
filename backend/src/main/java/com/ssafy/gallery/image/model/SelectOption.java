package com.ssafy.gallery.image.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SelectOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int selectOptionId;

    private int imageInfoId;

    private int optionId;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "imageInfoId")
//    private ImageInfo imageInfo;

    // Getters and setters omitted for brevity
}