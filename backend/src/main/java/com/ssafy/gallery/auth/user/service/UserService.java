package com.ssafy.gallery.auth.user.service;

import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.auth.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.ssafy.gallery.auth.oauth.client.OauthMemberClientComposite;
import com.ssafy.gallery.auth.user.model.User;
import com.ssafy.gallery.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final UserRepository userRepository;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    // 추가
    public int login(OauthServerType oauthServerType, String authCode) {

        User user = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        User saved = userRepository.findByDomain(user.domain())
                .orElseGet(() -> userRepository.save(user));
        return saved.userId();
    }
}