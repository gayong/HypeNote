package com.surf.editor.feign.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class QuizEditorSaveRequestDto {
    private String documentId;
    private int userId;
}
