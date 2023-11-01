package com.surf.diagram.diagram.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkResponseDto {
    private int source;
    private int target;
    private int userId;
}
