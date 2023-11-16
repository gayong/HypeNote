package com.surf.search.kakao.service;

import com.surf.search.common.error.ErrorCode;
import com.surf.search.common.error.exception.BaseException;
import com.surf.search.kakao.dto.KakaoSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class KakaoSearchService {

    @Value("${kakao.search.key}")
    private String key;
    private static String url = "https://dapi.kakao.com/v2/search/blog";

    public KakaoSearchRequestDto kakaoSearchGet(String query) {
        try{

            String prompt = "cs학습" + query;

            WebClient webClient = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("Authorization","KakaoAK "+key)
                    .build();

            KakaoSearchRequestDto webClientResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("")
                            .queryParam("query",prompt)
                            .queryParam("sort","accuracy")
                            .queryParam("size",30)
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(KakaoSearchRequestDto.class)
                    .block();

            List<KakaoSearchRequestDto.Items> Results = new ArrayList<>();

        // title에 대해 HTML 태그 제거
        if (webClientResponse != null && webClientResponse.getDocuments() != null) {
            for (KakaoSearchRequestDto.Items item : webClientResponse.getDocuments()) {
                if (item.getTitle() != null) {
                    item.setTitle(item.getTitle().replaceAll("(&lt;(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?&gt;)|(<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>)", ""));
                    item.setTitle(removeHtmlEntities(item.getTitle()));
                }

                // contents에 대해 HTML 태그 제거
                if (item.getContents() != null) {
                    item.setContents(item.getContents().replaceAll("(&lt;(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?&gt;)|(<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>)", ""));
                    item.setContents(removeHtmlEntities(item.getContents()));
                }

                // 사이트에서 나온 결과만을 필터링
                if (isSite(item.getUrl())) {
                    Results.add(item);
                }
            }
        }

            KakaoSearchRequestDto kakaoSearchRequestDto = KakaoSearchRequestDto.builder()
                    .documents(Results)
                    .build();
            return kakaoSearchRequestDto;

        }catch (Exception e){
            throw new BaseException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    private boolean isSite(String url) {
        return url != null && (url.contains("tistory.com") || (url.contains("velog.io")));
    }

    //HTML 엔티티 제거
    public static String removeHtmlEntities(String input) {
        // HTML 엔터티를 찾을 정규식
        String regex = "&#[0-9]+;|&[a-zA-Z]+;";

        // 정규식을 사용하여 매치된 엔터티 제거
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, "");
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }
}