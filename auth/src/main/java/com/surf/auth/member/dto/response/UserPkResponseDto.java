package com.surf.auth.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPkResponseDto {

    String message;
    private Data data;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {

        private int userPk;
        private String nickName;
        private String profileImage;
    }
}
