package com.surf.search.google.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.surf.search.common.error.ErrorCode;
import com.surf.search.common.error.exception.BaseException;
import com.surf.search.google.dto.SearchGetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SearchService {

    @Value("${google.search.search_engine_id}")
    private String SEARCH_ENGINE_ID;

    @Value("${google.search.api_key}")
    private String API_KEY;

    private static final String GOOGLE_URL = "https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s";

    @Autowired
    private ObjectMapper objectMapper;

    public SearchGetResponseDto searchGet(String query) {
        try{
            WebClient webClient = WebClient.create();
            String url = String.format(GOOGLE_URL,
                    API_KEY,SEARCH_ENGINE_ID, query);

            Mono<String> stringMono = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class);

            return stringMono.map(jsonString -> {
                        try{
                            return objectMapper.readValue(jsonString, SearchGetResponseDto.class);

                        }catch (Exception e){
                            throw new RuntimeException("failsed to parse Json",e);
                        }
                    }).block();
        }catch (Exception e){
            throw new BaseException(ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }
}
