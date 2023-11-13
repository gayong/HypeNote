package com.surf.editor.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class EditorUserListRequestDto {
    private List<String> sharedDocumentsRootList;
}
