package com.ssafy.gallery.auth.oauth.authcode;


import com.ssafy.gallery.auth.oauth.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}