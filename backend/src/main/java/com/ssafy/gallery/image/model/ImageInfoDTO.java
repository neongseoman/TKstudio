package com.ssafy.gallery.image.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ImageInfoDTO {
    private int imageInfoId;
    private int userId;
    private String originalImageUrl;
    private String thumbnailImageUrl;
    private String processedImageUrl;
    private LocalDateTime createdTime;
    private List<SelectOptionDTO> selectOptions;
}