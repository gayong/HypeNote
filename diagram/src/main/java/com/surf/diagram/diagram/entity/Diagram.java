package com.surf.diagram.diagram.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "diagram")
public class Diagram {
    @Transient
    public static final String SEQUENCE_NAME = "diagram_sequence";


    @Id
    private Long id;
    private String title;
    private String content;
    private String category;
    private String access = "Private";
    private Long x;
    private Long y;
    private Long z;

    private List<Diagram> children;

    public void setId(Long id) {
        this.id = id;
    }

    public void setChildren(List<Diagram> children) {
        this.children = children;
    }
}
