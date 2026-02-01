package com.example.ssccwebbe.domain.preuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
