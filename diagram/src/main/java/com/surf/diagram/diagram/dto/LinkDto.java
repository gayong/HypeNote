package com.surf.diagram.diagram.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LinkDto {

    private String id;
    private String source;
    private String target;
    private double similarity;
    private int userId;
}