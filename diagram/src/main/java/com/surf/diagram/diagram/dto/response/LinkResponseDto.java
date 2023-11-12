package com.surf.diagram.diagram.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinkResponseDto {
    private String source;
    private String target;
    private double similarity;
    private int userId;
}
