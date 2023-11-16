package com.surf.diagram.diagram.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
public class LinkDto {

    private String id;
    private String source;
    private String target;
    private double similarity;
    private int userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkDto linkDto = (LinkDto) o;
        return (source.equals(linkDto.source) && target.equals(linkDto.target)) ||
                (source.equals(linkDto.target) && target.equals(linkDto.source));
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target) + Objects.hash(target, source);
    }
}