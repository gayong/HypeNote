package com.surf.search.service;

import lombok.RequiredArgsConstructor;
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

    public Mono<String> searchGet(String query) {
        WebClient webClient = WebClient.create();
        String url = String.format(GOOGLE_URL,
                API_KEY, SEARCH_ENGINE_ID, query);

        Mono<String> stringMono = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);

        return stringMono;
    }
}
