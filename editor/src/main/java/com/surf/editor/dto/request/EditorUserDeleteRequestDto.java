package com.surf.editor.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class EditorUserDeleteRequestDto {
    private List<String> documentsIdRoots;
}
