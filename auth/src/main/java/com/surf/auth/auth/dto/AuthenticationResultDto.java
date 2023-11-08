package com.surf.auth.auth.dto;

import com.surf.auth.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResultDto {

    private boolean result = false;
    private UserDto userInfo;
}
