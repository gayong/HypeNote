package com.surf.search.kakao.controller;

import com.surf.search.common.response.ApiResponse;
import com.surf.search.kakao.dto.KakaoSearchRequestDto;
import com.surf.search.kakao.service.KakaoSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class KakaoSearchController {

    private final KakaoSearchService kakaoSearchService;

    @GetMapping("/kakao")
    public ResponseEntity<ApiResponse> kakaoSearchGet(@RequestParam String query){
        KakaoSearchRequestDto itemsList = kakaoSearchService.kakaoSearchGet(query);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("검색 결과")
                .status(HttpStatus.OK.value())
                .data(itemsList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
