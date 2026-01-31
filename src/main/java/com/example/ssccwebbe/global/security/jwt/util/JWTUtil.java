package com.example.ssccwebbe.global.security.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final SecretKey secretKey;
    private final Long accessTokenExpiresIn;
    private final Long refreshTokenExpiresIn;

    public JWTUtil(
            @Value("${jwt.secret-key}") String secretKeyString,
            @Value("${jwt.access-token-expires-in}") Long accessTokenExpiresIn,
            @Value("${jwt.refresh-token-expires-in}") Long refreshTokenExpiresIn) {
        this.secretKey = new SecretKeySpec(
                secretKeyString.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    // JWT 클레임 username 파싱
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("sub", String.class);
    }

    // JWT 클레임 role 파싱
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // JWT 유효 여부 (위조, 시간, Access/Refresh 여부)
    public Boolean isValid(String token, Boolean isAccess) {
        try {

            // claims 객체: JWT 토큰을 파싱한 결과로, 토큰에 저장된 모든 데이터(클레임)을 담고 있는 Map 형태의 객체
            // 파싱 과정중 만료된 토큰인 경우 JwtException이 발생함
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // JWT 토큰의 페이로드(Claims)에서 "type" 값을 String 타입으로 추출
            String type = claims.get("type", String.class);
            if (type == null) return false;

            if (isAccess && !type.equals("access")) return false;
            if (!isAccess && !type.equals("refresh")) return false;

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    // JWT(Access/Refresh) 생성
    public String createJWT(String username, String role, Boolean isAccess) {

        long now = System.currentTimeMillis();
        long expiry = isAccess ? accessTokenExpiresIn : refreshTokenExpiresIn;
        String type = isAccess ? "access" : "refresh";

        return Jwts.builder()
                .claim("sub", username)
                .claim("role", role)
                .claim("type", type) // isValid 메서드해서 해당 값을 확인함
                .issuedAt(new Date(now))
                .expiration(new Date(now + expiry))
                .signWith(secretKey)
                .compact();
    }
}