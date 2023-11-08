package com.surf.editor.dto.request;

import lombok.Getter;

@Getter
public class EditorShareRequestDto {
    private int userId;
    private String editorId;
    private String sharedNickname;
}
