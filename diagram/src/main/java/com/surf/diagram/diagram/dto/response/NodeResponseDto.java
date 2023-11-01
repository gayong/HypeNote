package com.surf.diagram.diagram.dto.response;


import com.surf.diagram.diagram.entity.Link;
import com.surf.diagram.diagram.entity.Node;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@Getter
@Setter
public class NodeResponseDto {
    private List<Node> nodes;
    private List<Link> links;
}
