package com.example.ssccwebbe.domain.preuser.service;

import com.example.ssccwebbe.domain.preuser.dto.PreUserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface PreUserService extends  OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    PreUserResponseDto readPreUser();
}
