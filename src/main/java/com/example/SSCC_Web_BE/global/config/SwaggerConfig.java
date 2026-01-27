package com.example.SSCC_Web_BE.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("SSCC Server API")
                                .description(
                                        """
                        SSCC 웹사이트 제작 프로젝트 백엔드 API 문서입니다.
                        - 본 문서는 SSCC 서버에서 제공하는 REST API 명세를 정의합니다.
                        """)
                                .version("v1.0.0"));
    }
}
