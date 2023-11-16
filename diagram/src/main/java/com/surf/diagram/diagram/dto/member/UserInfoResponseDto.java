package com.surf.diagram.diagram.dto.member;


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
    private List<String> sharedDocumentsRoots;
    private String role;
}
