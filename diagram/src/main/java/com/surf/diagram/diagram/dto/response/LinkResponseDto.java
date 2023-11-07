package com.surf.diagram.diagram.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinkResponseDto {
    private int source;
    private int target;
    private double similarity;
    private int userId;
}
