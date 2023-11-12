package com.surf.diagram.diagram.dto.editor;

import lombok.Getter;

import java.util.List;

@Getter
public class EditorListRequestDto {
    private final List<String> rootList;

    public EditorListRequestDto(List<String> lst){
        this.rootList = lst;
    }
}

