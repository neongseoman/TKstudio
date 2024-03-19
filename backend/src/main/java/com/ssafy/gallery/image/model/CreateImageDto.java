package com.ssafy.gallery.image.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateImageDto {
    private int imageInfoId;

    private String thumbnailImageUrl;

    private String originalImageUrl;

    private String processedImageUrl;
}
