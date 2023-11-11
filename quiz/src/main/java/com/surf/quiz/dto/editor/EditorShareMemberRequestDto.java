package com.surf.quiz.dto.editor;

import lombok.Getter;

import java.util.List;

@Getter
public class EditorShareMemberRequestDto {
    private final List<String> editorList;

    public EditorShareMemberRequestDto(List<String> pages) {
        this.editorList = pages;
    }
}


