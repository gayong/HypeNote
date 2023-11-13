package com.surf.editor.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class EditorUploadResponseDto {
    private String url;
}
