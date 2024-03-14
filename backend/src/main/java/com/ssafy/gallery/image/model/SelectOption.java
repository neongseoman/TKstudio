package com.ssafy.gallery.image.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelectOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SELECT_OPTION_ID")
    private int selectOptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_INFO_ID", nullable = false)
    private ImageInfo imageInfo;

    @Column(name = "OPTION_ID", nullable = false)
    private int optionId;

    @CreatedDate
    private LocalDateTime createTime;

    // 모든 필드를 초기화하는 생성자
    public SelectOption(ImageInfo imageInfo, int optionId) {
        this.imageInfo = imageInfo;
        this.optionId = optionId;
    }
}