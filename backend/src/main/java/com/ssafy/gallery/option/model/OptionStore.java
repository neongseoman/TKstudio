package com.ssafy.gallery.option.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
public class OptionStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int optionId;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET UTF8")
    private String optionName;
    private int cost;
    private String optionS3Url;
    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET UTF8")
    private String discription;
    private int categoryId;
    @Setter
    @ColumnDefault("false")
    private boolean purchased;
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
