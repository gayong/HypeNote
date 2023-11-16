package com.surf.editor.feign.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class MemberShareRequestDto {
    private List<Integer> userPkList;
    private String documentId;
}
