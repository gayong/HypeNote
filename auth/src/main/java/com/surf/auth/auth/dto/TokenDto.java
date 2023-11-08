package com.surf.auth.auth.dto;

import com.surf.auth.auth.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String message;
    private String accessToken = null;
    private UserDto userInfo;
}