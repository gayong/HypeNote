package com.surf.editor.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class EditorUserListResponseDto {
    private List<Integer> userList;
}
