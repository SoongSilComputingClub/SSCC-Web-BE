package com.example.ssccwebbe.apitest.controller;

import jakarta.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ssccwebbe.apitest.dto.TestTokenResponseDto;
import com.example.ssccwebbe.apitest.service.TestService;
import com.example.ssccwebbe.global.apipayload.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Validated
@Tag(name = "Test API", description = "개발/테스트용 API (프로덕션에서는 사용 불가)")
public class TestController {

    private final TestService testService;

    @GetMapping
    @Operation(
            summary = "테스트용 토큰 발급",
            description =
                    "username만으로 유저를 생성하거나 조회하여 access/refresh 토큰을 발급합니다. 이미 존재하는 username이면 새로운"
                            + " 토큰만 발급합니다.")
    public ApiResponse<TestTokenResponseDto> getTestTokens(
            @RequestParam @NotBlank(message = "username은 필수입니다") String username) {

        TestTokenResponseDto tokens = testService.createOrGetTokens(username);
        return ApiResponse.success(tokens);
    }

    @GetMapping("/preuser-only")
    @Operation(
            summary = "PREUSER 권한 테스트",
            description = "PREUSER 권한을 가진 토큰으로만 접근 가능한 테스트 엔드포인트입니다.")
    public ApiResponse<String> preuserOnlyTest() {
        return ApiResponse.success("PREUSER 권한 테스트 성공! 🎉");
    }
}
