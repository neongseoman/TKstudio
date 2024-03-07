package com.ssafy.gallery.auth.oauth.client;

import com.ssafy.gallery.auth.oauth.OauthServerType;
import com.ssafy.gallery.auth.oauth.dto.OauthMember;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthMember fetch(String code);
}