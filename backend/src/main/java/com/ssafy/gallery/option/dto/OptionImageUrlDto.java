package com.ssafy.gallery.option.dto;

import com.ssafy.gallery.option.model.OptionStore;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OptionImageUrlDto {
    private int optionId;
    private String optionS3Url;

    public OptionImageUrlDto(OptionStore optionStore) {
        this.optionId = optionStore.getOptionId();
        this.optionS3Url = optionStore.getOptionS3Url();
    }
}
