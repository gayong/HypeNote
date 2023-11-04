package com.surf.diagram.diagram.dto.editor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@ToString
public class EditorCheckResponse {
    private String id;
    private String title;
    private String content;
}
