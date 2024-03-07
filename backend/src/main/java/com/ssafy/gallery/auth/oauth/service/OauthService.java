package com.ssafy.gallery.auth.oauth.service;

import com.ssafy.gallery.auth.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.ssafy.gallery.auth.oauth.OauthServerType;
import com.ssafy.gallery.auth.oauth.client.OauthMemberClientComposite;
import com.ssafy.gallery.auth.oauth.dto.OauthMember;
import com.ssafy.gallery.auth.oauth.repository.OauthMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    // 추가
    public Long login(OauthServerType oauthServerType, String authCode) {

        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        System.out.println(oauthMember.nickname());
        System.out.println(oauthMember.oauthId().oauthServerId());
        System.out.println(oauthMember.oauthId().oauthServer());

        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
        return saved.id();
    }
}