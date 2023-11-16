package com.surf.auth.member.handler;

import com.surf.auth.member.dto.response.UserInfoResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserNotFoundExceptionHandler {
    public UserInfoResponseDto userNotFound () {

        return UserInfoResponseDto.builder()
                .message("해당 유저가 존재하지 않습니다.")
                .build();
    }
}
