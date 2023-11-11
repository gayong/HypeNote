package com.surf.search.naver.controller;

import com.surf.search.common.response.ApiResponse;
import com.surf.search.naver.dto.NaverSearchRequestDto;
import com.surf.search.naver.service.NaverSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class NaverSearchController {

    private final NaverSearchService naverSearchService;

    @GetMapping("/naver")
    public ResponseEntity<ApiResponse> naverSearchGet(@RequestParam String query){
        NaverSearchRequestDto naverSearchGet  = naverSearchService.naverSearchGet(query);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("검색 결과")
                .status(HttpStatus.OK.value())
                .data(naverSearchGet)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
