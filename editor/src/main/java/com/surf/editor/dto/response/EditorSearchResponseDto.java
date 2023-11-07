package com.surf.editor.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class EditorSearchResponseDto {
    List<Editors> notes;

    @Getter
    @Builder(toBuilder = true)
    public static class Editors{
        private String id;
        private String title;
        private String content;
    }


}
