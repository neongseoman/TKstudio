package com.ssafy.gallery.auth.user.model;

import com.ssafy.gallery.auth.oauth.dto.Domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
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

    private String gender;
    private LocalDateTime registerTime;

    public int userId() {
        return userId;
    }

    public Domain domain() {
        return domain;
    }

    public String nickname() {
        return nickname;
    }

    public String gender() {
        return gender;
    }

    @PrePersist
    protected void onCreate() {
        registerTime = LocalDateTime.now();
    }
}