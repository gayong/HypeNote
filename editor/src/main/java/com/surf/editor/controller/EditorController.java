package com.surf.editor.controller;

import com.surf.editor.common.response.ApiResponse;
import com.surf.editor.dto.request.*;
import com.surf.editor.dto.response.*;
import com.surf.editor.service.EditorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/editor")
@RequiredArgsConstructor
public class EditorController {

    final private EditorService editorService;

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> editorCreate(@PathVariable int userId){
        EditorCreateResponseDto editorCreateResponseDto = editorService.editorCreate(userId);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 작성 성공")
                .status(OK.value())
                .data(editorCreateResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/relation/{userId}")
    public ResponseEntity<ApiResponse> editorRelation(@PathVariable int userId, @RequestBody EditorRelationRequestDto editorRelationRequestDto){
        editorService.editorRelation(userId,editorRelationRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 연관 관계 연결 성공")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/hyperlink/{userId}")
    public ResponseEntity<ApiResponse> editorHyperLink(@PathVariable int userId, @RequestBody EditorHyperLinkRequestDto editorHyperLinkRequestDto){
        editorService.editorHyperLink(userId,editorHyperLinkRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 하이퍼 링크 연결 성공")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/write/{editorId}")
    public ResponseEntity<ApiResponse> editorWrite(@PathVariable String editorId, @RequestBody @Valid EditorWriteRequestDto editorWriteRequest){
        editorService.editorWrite(editorId,editorWriteRequest);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 글 쓰기")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{editorId}")
    public ResponseEntity<ApiResponse> editorDelete(@PathVariable String editorId){
        editorService.editorDelete(editorId);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 글 삭제")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{editorId}")
    public ResponseEntity<ApiResponse> editorCheck(@PathVariable String editorId){
        EditorCheckResponseDto editorCheckResponse = editorService.editorCheck(editorId);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 조회")
                .status(OK.value())
                .data(editorCheckResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> editorSearch(@RequestParam(value = "query")String search){
        EditorSearchResponseDto editorSearchResponse = editorService.editorSearch(search);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 검색")
                .status(OK.value())
                .data(editorSearchResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/permission")
    public ResponseEntity<ApiResponse> editorWriterPermission(@RequestBody EditorWriterPermissionRequestDto editorWriterPermissionRequestDto){
        editorService.editorWriterPermission(editorWriterPermissionRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("쓰기 권한 설정 완료")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse> editorList(@RequestBody EditorListRequestDto editorListRequestDto){
        List<EditorListResponseDto> editorListResponseDtoList = editorService.editorList(editorListRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 리스트 출력")
                .status(OK.value())
                .data(editorListResponseDtoList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/share")
    public ResponseEntity<ApiResponse> editorShareMember(@RequestBody EditorShareMemberRequestDto editorShareMemberRequestDto){
        EditorShareMemberResponseDto editorShareMember = editorService.editorShareMember(editorShareMemberRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("공유한 사람 리스트 출력")
                .status(OK.value())
                .data(editorShareMember)
                .build();

        return ResponseEntity.ok(apiResponse);

    }
}
