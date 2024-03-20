package com.ssafy.gallery.image.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ssafy.gallery.option.model.OptionStore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SelectOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int selectOptionId;

    @ManyToOne
    @JoinColumn(name = "image_info_id")
    private ImageInfo imageInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private OptionStore optionId;

    public void changeSelectOption(ImageInfo imageInfo){
        imageInfo = this.imageInfo;
        imageInfo.addSelectOption(this);
    }

    // 생성자, 필요한 메서드 등 추가
}