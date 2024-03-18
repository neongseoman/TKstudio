package com.ssafy.gallery.image.model;

import com.ssafy.gallery.option.model.OptionStore;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class SelectOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int selectOptionId;

    @ManyToOne
    @JoinColumn(name = "image_info_id")
    private ImageInfo imageInfo;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private OptionStore optionId;

    // 생성자, 필요한 메서드 등 추가
}