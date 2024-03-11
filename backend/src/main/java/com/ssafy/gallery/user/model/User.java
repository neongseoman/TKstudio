package com.ssafy.gallery.user.model;

import com.ssafy.gallery.auth.oauth.dto.Domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "domain_unique",
                        columnNames = {
                                "domain_user_key",
                                "domain_name"
                        }
                ),
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Embedded
    private Domain domain;

    @Column(name = "nickname", nullable = false, columnDefinition = "VARCHAR(255) CHARACTER SET UTF8")
    private String nickname;

    private String email;
    private String gender;
    private LocalDateTime registerTime;

    @PrePersist
    protected void onCreate() {
        registerTime = LocalDateTime.now();
    }
}