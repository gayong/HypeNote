package com.surf.search.google.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchGetResponseDto {
    private List<ItemsResponseDto> items;
}
