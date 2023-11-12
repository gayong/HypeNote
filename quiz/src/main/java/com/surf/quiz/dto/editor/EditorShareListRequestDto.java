package com.surf.quiz.dto.editor;

import lombok.Getter;

import java.util.List;

@Getter
public class EditorShareListRequestDto {
    private final List<String> editorIds;


    public EditorShareListRequestDto(List<String> pages) {
        this.editorIds = pages;
    }
}
