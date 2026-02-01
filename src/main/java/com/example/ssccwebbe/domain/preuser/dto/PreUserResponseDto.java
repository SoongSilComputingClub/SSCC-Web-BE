package com.example.ssccwebbe.domain.preuser.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreUserResponseDto {
    private String username;
    private Boolean social;
    private String nickname;
    private String email;
}
