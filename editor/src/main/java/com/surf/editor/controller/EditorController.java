package com.surf.editor.controller;

import com.surf.editor.common.response.ApiResponse;
import com.surf.editor.service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/editor")
@RequiredArgsConstructor
public class EditorController {

    final private EditorService editorService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> editorCreate(@PathVariable String userId){
        editorService.editorCreate(userId);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("게시판 작성")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
