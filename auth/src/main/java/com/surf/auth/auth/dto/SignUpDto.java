package com.surf.auth.auth.dto;

import lombok.Getter;

@Getter
public class SignUpDto {

    private String email;
    private String password;
    private String nickName;
    private String profileImage;
}
