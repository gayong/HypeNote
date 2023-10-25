package com.surf.editor.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class EditorSearchResponse {
    List<Editors> editorsList;

    @Getter
    @Builder(toBuilder = true)
    public static class Editors{
        private String title;
        private String content;
    }


}
