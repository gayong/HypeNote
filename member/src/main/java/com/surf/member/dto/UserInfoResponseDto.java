package com.surf.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDto {

    private String message;

    private int userPk;
    private String email;
    private String nickName;
    private String profileImage;
    private List<String> documentsRoots;
    private String role;
}
