package com.surf.quiz.dto.editor;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder(toBuilder = true)
@ToString
public class EditorCheckResponse {
    private String id;
    private String title;
    private String content;
}
