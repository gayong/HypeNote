package com.surf.search.naver.service;

import com.surf.search.common.error.ErrorCode;
import com.surf.search.common.error.exception.BaseException;
import com.surf.search.naver.dto.NaverSearchRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class NaverSearchService {

//    @Value("${naver.search.naver_clientId}")
    private String naverClientId;
//    @Value("${naver.search.naver_client_secret}")
    private String naverClientSecret;

    private static String url = "https://openapi.naver.com/v1/search/blog.json";

    public NaverSearchRequestDto naverSearchGet(String query) {
        try{

            String queryPlus = "cs 학습"+query;

            WebClient webClient = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("X-Naver-Client-Id",naverClientId)
                    .defaultHeader("X-Naver-Client-Secret",naverClientSecret)
                    .build();

            NaverSearchRequestDto webClientResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("")
                            .queryParam("query",queryPlus)
                            .queryParam("display", 10)
                            .queryParam("start", 1)
                            .queryParam("sort", "sim")
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(NaverSearchRequestDto.class)
                    .block();

            // title에 대해 HTML 태그 제거
            if (webClientResponse != null && webClientResponse.getItems() != null) {
                for (NaverSearchRequestDto.BlogPostItemDTO item : webClientResponse.getItems()) {
                    if (item.getTitle() != null) {
                        item.setTitle(item.getTitle().replaceAll("(&lt;(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?&gt;)|(<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>)", ""));
                        item.setTitle(removeHtmlEntities(item.getTitle()));
                    }

                    // contents에 대해 HTML 태그 제거
                    if (item.getDescription() != null) {
                        item.setDescription(item.getDescription().replaceAll("(&lt;(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?&gt;)|(<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>)", ""));
                        item.setDescription(removeHtmlEntities(item.getDescription()));
                    }
                }
            }


            return webClientResponse;

        }catch (Exception e){
            throw new BaseException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
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
