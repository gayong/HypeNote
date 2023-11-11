package com.surf.search.kakao.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class KakaoSearchRequestDto {

    private List<Items> documents;

    @Getter
    @Setter
    public static class Items{
        private String title;
        private String contents;
        private String url;
        private String blogname;
        private String thumbnail;
        private String datetime;
    }

}
