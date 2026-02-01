package com.example.ssccwebbe.domain.preuser.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.ssccwebbe.domain.preuser.dto.PreUserResponseDto;

public interface PreUserService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    PreUserResponseDto readPreUser();
}
