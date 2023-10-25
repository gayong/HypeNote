package com.surf.editor.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder(toBuilder = true)
public class EditorCheckResponse {
    private String id;
    private String title;
    private String content;
}
