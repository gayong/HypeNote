package com.surf.auth.JWT.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}
