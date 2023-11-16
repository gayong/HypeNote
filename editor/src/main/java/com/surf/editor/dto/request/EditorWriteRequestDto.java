package com.surf.editor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EditorWriteRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
