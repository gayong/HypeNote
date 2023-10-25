package com.surf.editor.controller;

import com.surf.editor.common.response.ApiResponse;
import com.surf.editor.dto.request.EditorWriteRequest;
import com.surf.editor.dto.response.EditorCheckResponse;
import com.surf.editor.service.EditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/write/{editorId}")
    public ResponseEntity<ApiResponse> editorWrite(@PathVariable String editorId, @RequestBody EditorWriteRequest editorWriteRequest){
        editorService.editorWrite(editorId,editorWriteRequest);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("게시판 글 쓰기")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{editorId}")
    public ResponseEntity<ApiResponse> editorDelete(@PathVariable String editorId){
        editorService.editorDelete(editorId);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("게시판 글 삭제")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{editorId}")
    public ResponseEntity<ApiResponse> editorCheck(@PathVariable String editorId){
        EditorCheckResponse editorCheckResponse = editorService.editorCheck(editorId);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("게시글 조회")
                .status(OK.value())
                .data(editorCheckResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
