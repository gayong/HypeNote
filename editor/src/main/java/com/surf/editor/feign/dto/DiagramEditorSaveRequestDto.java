package com.surf.editor.feign.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class DiagramEditorSaveRequestDto {
    private String documentId;
    private int userId;
    private String title;
    private String content;
}
