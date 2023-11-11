package com.surf.editor.feign.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MemberShareRequestDto {
    private int userPk;
    private String editorId;
}
