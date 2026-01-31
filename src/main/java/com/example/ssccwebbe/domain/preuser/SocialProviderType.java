package com.example.ssccwebbe.domain.preuser;

import lombok.Getter;

@Getter
public enum SocialProviderType {
    GOOGLE("구글");
    private final String description;

    SocialProviderType(String description) {
        this.description = description;
    }
}
