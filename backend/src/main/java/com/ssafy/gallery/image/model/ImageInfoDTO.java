package com.ssafy.gallery.image.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ImageInfoDTO {
    private int imageInfoId;
    private LocalDateTime createdTime;
    private List<Integer> selectOptionDTOList;

    public ImageInfoDTO(ImageInfo imageInfo) {
        this.imageInfoId = imageInfo.getImageInfoId();
        this.createdTime = imageInfo.getCreatedTime();
        this.selectOptionDTOList = imageInfo.getSelectOptions().stream()
                .map(s->s.getSelectOptionId())
                .collect(Collectors.toList());
    }
}