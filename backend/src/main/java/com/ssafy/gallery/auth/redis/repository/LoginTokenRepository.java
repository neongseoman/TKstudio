package com.ssafy.gallery.auth.redis.repository;


import com.ssafy.gallery.auth.redis.dto.LoginTokenDto;
import org.springframework.data.repository.CrudRepository;

public interface LoginTokenRepository extends CrudRepository<LoginTokenDto, String> {
}