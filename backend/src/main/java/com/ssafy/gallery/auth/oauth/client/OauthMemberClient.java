package com.ssafy.gallery.auth.oauth.client;

import com.ssafy.gallery.auth.oauth.type.OauthServerType;
import com.ssafy.gallery.user.model.User;

public interface OauthMemberClient {

    OauthServerType supportServer();

    User fetch(String code);
}