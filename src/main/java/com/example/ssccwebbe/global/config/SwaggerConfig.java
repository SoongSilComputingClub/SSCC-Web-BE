package com.example.ssccwebbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        String jwtSchemeName = "JWT TOKEN";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components =
                new Components()
                        .addSecuritySchemes(
                                jwtSchemeName,
                                new SecurityScheme()
                                        .name(jwtSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"));

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components)
                .paths(createCustomPaths());
    }

    private Paths createCustomPaths() {
        return new Paths().addPathItem("/logout", createLogoutPath());
    }

    private PathItem createLogoutPath() {
        return new PathItem()
                .post(
                        new Operation()
                                .summary("로그아웃")
                                .description(
                                        "Refresh 토큰을 서버에서 삭제하여 로그아웃 처리합니다. 토큰이 유효하지 않아도 성공 응답을"
                                                + " 반환합니다.")
                                .addTagsItem("JWT API")
                                .requestBody(createLogoutRequestBody())
                                .responses(createLogoutResponses()));
    }

    private RequestBody createLogoutRequestBody() {
        ObjectSchema requestSchema = new ObjectSchema();
        requestSchema.addProperty("refreshToken", new StringSchema().description("Refresh 토큰"));
        requestSchema.required(java.util.List.of("refreshToken"));

        return new RequestBody()
                .required(true)
                .content(
                        new Content()
                                .addMediaType(
                                        "application/json", new MediaType().schema(requestSchema)));
    }

    private ApiResponses createLogoutResponses() {
        return new ApiResponses()
                .addApiResponse(
                        "200",
                        new ApiResponse()
                                .description("로그아웃 성공")
                                .content(
                                        new Content()
                                                .addMediaType(
                                                        "application/json",
                                                        new MediaType()
                                                                .schema(
                                                                        createSuccessResponseSchema()))));
    }

    private ObjectSchema createSuccessResponseSchema() {
        ObjectSchema schema = new ObjectSchema();
        schema.addProperty(
                "success", new io.swagger.v3.oas.models.media.BooleanSchema().example(true));
        schema.addProperty("code", new StringSchema().example("COMMON200"));
        schema.addProperty("message", new StringSchema().example("요청이 성공적으로 처리되었습니다."));
        schema.addProperty("data", new ObjectSchema().nullable(true).example(null));
        return schema;
    }

    private Info apiInfo() {
        return new Info()
                .title("SSCC Web BE API")
                .description("SSCC 웹서비스 API 명세서")
                .version("1.0.0");
    }
}
