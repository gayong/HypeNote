package com.surf.diagram.diagram.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NodeResponseDto {
    private Long id;
    private String title;
    private int userId;      // group
    private int editorId;
}
