package com.surf.auth.security.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class SignUpDto {

    private String email;
    private String password;
    private String nickName;
}
