package com.surf.editor.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class EditorUnShareRequestDto {
    private int userId;
    private String editorId;
    private List<Integer> userList;
}
