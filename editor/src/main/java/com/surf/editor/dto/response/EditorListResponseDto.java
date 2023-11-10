package com.surf.editor.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditorListResponseDto {
    private String id;
    private String title;
    private String parentId;
    private List<EditorListResponseDto> children;

    public EditorListResponseDto(){
    }
}
