package com.surf.diagram.diagram.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
public class Link {

    private String id;
    private String source;
    private String target;
    private double similarity;
    private int userId;
}