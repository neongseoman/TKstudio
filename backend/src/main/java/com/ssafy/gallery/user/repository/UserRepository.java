package com.ssafy.gallery.user.repository;

import com.ssafy.gallery.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}