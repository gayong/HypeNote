package com.surf.auth.auth.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int userPk;
    private String email;
    private String nickName;
    private String profileImage;
    private List<String> documentsRoots;
    private String role;
}
