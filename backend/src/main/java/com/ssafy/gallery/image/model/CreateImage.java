package com.ssafy.gallery.image.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateImage {

    private String thumbnailImageUrl;

    private String originalImageUrl;

    private String processedImageUrl;
}
