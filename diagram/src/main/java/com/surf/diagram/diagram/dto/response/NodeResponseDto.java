package com.surf.diagram.diagram.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NodeResponseDto {
    private String id;
    private String title;
    private int group;      // group
    private String editorId;
}
