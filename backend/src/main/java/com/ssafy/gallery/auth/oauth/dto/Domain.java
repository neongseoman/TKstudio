package com.ssafy.gallery.auth.oauth.dto;

import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Domain {

    @Column(nullable = false, name = "domain_user_key")
    private String domainUserKey;

    @Enumerated(STRING)
    @Column(nullable = false, name = "domain_name")
    private OauthServerType domainName;

    public String domainUserKey() {
        return domainUserKey;
    }

    public OauthServerType domainName() {
        return domainName;
    }
}