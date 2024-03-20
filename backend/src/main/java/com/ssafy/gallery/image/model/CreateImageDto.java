package com.ssafy.gallery.image.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateImageDto {
    private int imageInfoId;

    private String thumbnailImageUrl;

    private String originalImageUrl;

    private String processedImageUrl;
    private ByteArrayResource resource = null;
}
