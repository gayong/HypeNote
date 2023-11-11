package com.surf.editor.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EditorShareMemberResponseDto {
    private List<Integer> userList;
}
