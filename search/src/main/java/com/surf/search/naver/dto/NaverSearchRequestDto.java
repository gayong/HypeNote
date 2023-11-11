package com.surf.search.naver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class NaverSearchRequestDto {

    private BlogPostItemDTO[] items;

    @Getter
    @Setter
    public static class BlogPostItemDTO {

            private String title;
            private String link;
            private String description;
            private String bloggername;
            private String bloggerlink;
            private String postdate;
        }

}
