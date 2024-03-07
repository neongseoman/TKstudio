package com.ssafy.gallery.auth.oauth.authcode;


import com.ssafy.gallery.auth.oauth.type.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}