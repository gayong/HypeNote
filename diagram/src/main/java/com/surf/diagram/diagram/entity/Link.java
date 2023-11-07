package com.surf.diagram.diagram.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "links")
public class Link {

    @Id
    private String id;
    private int source;
    private int target;
    private double similarity;
    private int userId;
}