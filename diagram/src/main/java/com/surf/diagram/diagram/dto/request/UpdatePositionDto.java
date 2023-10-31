package com.surf.diagram.diagram.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePositionDto {
    private Long x;
    private Long y;
    private Long z;
}
