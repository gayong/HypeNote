package com.surf.diagram.diagram.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeDto {

    private String id;
    private int userId;      // group
    private String title;
    private String editorId;
    private String content;
    private String category;

}