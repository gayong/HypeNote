package com.surf.editor.feign.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberListOpenFeignResponseDto {
    private String message;
    private Long userPk;
    private String email;
    private String nickName;
    private String profileImage;
    private List<String> documentsRoots;
    private List<String> sharedDocumentsRoots;
    private String role;
}
