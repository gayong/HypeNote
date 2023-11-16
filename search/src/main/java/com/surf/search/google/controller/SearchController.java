package com.surf.search.google.controller;

import com.surf.search.common.response.ApiResponse;
import com.surf.search.google.dto.SearchGetResponseDto;
import com.surf.search.google.service.SearchService;
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
public class SearchController {

    private final SearchService searchService;
    @GetMapping("")
    public ResponseEntity<ApiResponse> searchGet(@RequestParam String query){
        SearchGetResponseDto searchGet = searchService.searchGet(query);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("검색 결과")
                .status(HttpStatus.OK.value())
                .data(searchGet)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
