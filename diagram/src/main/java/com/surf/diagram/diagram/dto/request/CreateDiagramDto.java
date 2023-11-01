package com.surf.diagram.diagram.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CreateDiagramDto {
    private String title;
    private String content;
}