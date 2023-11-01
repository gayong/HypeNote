package com.surf.diagram.diagram.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NodeDto {
    private Long id;
    private int userId;      // group
    private String title;
    private int editorId;
}
