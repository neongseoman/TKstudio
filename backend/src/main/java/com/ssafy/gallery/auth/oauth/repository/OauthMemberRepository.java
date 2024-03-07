package com.ssafy.gallery.auth.oauth.repository;

import com.ssafy.gallery.auth.oauth.dto.OauthId;
import com.ssafy.gallery.auth.oauth.dto.OauthMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthMemberRepository extends JpaRepository<OauthMember, Long> {

    Optional<OauthMember> findByOauthId(OauthId oauthId);
}