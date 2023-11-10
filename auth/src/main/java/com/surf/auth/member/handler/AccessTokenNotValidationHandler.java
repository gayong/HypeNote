package com.surf.auth.member.handler;

import com.surf.auth.member.dto.UserInfoResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenNotValidationHandler {

    public UserInfoResponseDto accessTokenNotValidation () {

        return UserInfoResponseDto.builder()
                .message("유효하지 않은 토큰입니다.")
                .build();
    }
}
