package com.surf.editor.dto.request;

import lombok.Getter;

@Getter
public class EditorWriterPermissionRequestDto {
    private int userId;
    private int sharedId;
    private String editorId;
    private boolean writePermission;
}
