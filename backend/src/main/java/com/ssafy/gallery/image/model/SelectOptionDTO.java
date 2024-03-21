package com.ssafy.gallery.image.model;

import com.ssafy.gallery.option.model.OptionStore;
import lombok.*;

@Getter
@Setter
public class SelectOptionDTO {
    private int selectOptionId;
    private ImageInfo imageInfoId;
    private OptionStore optionId;
    public SelectOptionDTO(SelectOption selectOption) {
        this.selectOptionId = selectOption.getSelectOptionId();
        this.imageInfoId = selectOption.getImageInfo();
        this.optionId = selectOption.getOptionId();
    }
}