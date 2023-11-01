package com.surf.diagram.diagram.dto.response;


import com.surf.diagram.diagram.entity.Link;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NodeResponseDto {
    private List<NodeDto> nodes;
    private List<Link> links;

}
