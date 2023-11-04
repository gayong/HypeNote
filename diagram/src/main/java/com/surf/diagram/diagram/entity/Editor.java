package com.surf.diagram.diagram.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "editor")
public class Editor {
    private String editorId;
    private String title;
    private String content;
    private int userPk;
}
