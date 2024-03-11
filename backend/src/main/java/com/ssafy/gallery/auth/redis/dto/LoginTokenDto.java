package com.ssafy.gallery.auth.redis.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash(value = "token")
@AllArgsConstructor
@Builder
public class LoginTokenDto {

    @Id
    private String id;
    private int userId;
    private String tokenType;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private long expiration;

}
