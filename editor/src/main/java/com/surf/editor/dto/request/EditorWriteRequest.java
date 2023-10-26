package com.surf.editor.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EditorWriteRequest {
    @NotNull
    private String title;
    @NotNull
    private String content;
}
