package com.surf.search.google.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ItemsResponseDto {

    private String title;
    private String link;
    private String snippet;
    private PageMapDto pagemap;

    @Getter
    public static class PageMapDto{
        private List<CseThumbnailDto> cse_thumbnail;
    }

    @Getter
    public static class CseThumbnailDto{
        private String src;
        private Integer width;
        private Integer height;
    }
}
