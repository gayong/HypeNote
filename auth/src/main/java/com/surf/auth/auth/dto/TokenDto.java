package com.surf.auth.auth.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String message;
    private String accessToken = null;
    private String refreshToken = null;
}