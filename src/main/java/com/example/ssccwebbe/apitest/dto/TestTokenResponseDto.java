package com.example.ssccwebbe.apitest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTokenResponseDto {

    private String accessToken;
    private String refreshToken;
}
