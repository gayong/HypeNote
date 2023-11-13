package com.surf.diagram.diagram.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
public class Node {

    private String id;
    private int userId;      // group
    private String title;
    private String editorId;
    private String content;
    private String category;

}