package com.surf.editor.feign.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class MemberListOpenFeignRequestDto {
    private List<Integer> userPkList;
}
