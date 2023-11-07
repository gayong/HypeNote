package com.surf.editor.feign.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class MemberShareRequestDto {
    private int userId;
    private String root;
}
