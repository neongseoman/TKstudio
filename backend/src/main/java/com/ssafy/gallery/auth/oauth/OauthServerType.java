package com.ssafy.gallery.auth.oauth;

import static java.util.Locale.ENGLISH;

public enum OauthServerType {

    kakao,
    ;

    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toLowerCase(ENGLISH));
    }
}
