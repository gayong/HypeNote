package com.surf.editor.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class EditorCheckResponseDto {
    private String id;
    private String title;
    private String content;
    private int owner;
}
