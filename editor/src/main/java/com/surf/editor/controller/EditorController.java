package com.surf.editor.controller;

import com.surf.editor.common.response.ApiResponse;
import com.surf.editor.dto.request.*;
import com.surf.editor.dto.response.*;
import com.surf.editor.service.EditorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/child/{userId}")
    public ResponseEntity<ApiResponse> editorChildCreate(@PathVariable int userId,@RequestBody EditorChildCreateRequestDto editorChildCreateRequestDto){
        EditorCreateResponseDto editorCreateResponseDto = editorService.editorChildCreate(userId,editorChildCreateRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("자식 문서 작성 성공")
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

    @GetMapping("/search/{userId}")
    public ResponseEntity<ApiResponse> editorSearch(@RequestParam(value = "query")String search, @PathVariable int userId){
        EditorSearchResponseDto editorSearchResponse = editorService.editorSearch(search,userId);

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

    @PostMapping("/member")
    public ResponseEntity<ApiResponse> editorShareMember(@RequestBody EditorShareMemberRequestDto editorShareMemberRequestDto){
        EditorShareMemberResponseDto editorShareMember = editorService.editorShareMember(editorShareMemberRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("공유한 사람 리스트 출력")
                .status(OK.value())
                .data(editorShareMember)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/share")
    public ResponseEntity<ApiResponse> editorShare(@RequestBody EditorShareRequestDto editorShareRequestDto){
        editorService.editorShare(editorShareRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 공유 완료")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/unshare")
    public ResponseEntity<ApiResponse> editorUnShare(@RequestBody EditorUnShareRequestDto editorUnShareRequestDto){
        editorService.editorUnshare(editorUnShareRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("문서 공유 해제")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/shareList")
    public ResponseEntity<ApiResponse> editorShareList(@RequestBody EditorShareListRequestDto editorShareListRequestDto){
        EditorShareListResponseDto editorShareList= editorService.editorShareList(editorShareListRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("공유 유저 전체 리스트")
                .status(OK.value())
                .data(editorShareList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/root-delete")
    public ResponseEntity<ApiResponse> editorUserDelete(@RequestBody EditorUserDeleteRequestDto editorUserDeleteRequestDto){
        editorService.editorUserDelete(editorUserDeleteRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("유저 관련 문서 삭제 완료")
                .status(OK.value())
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/root-owner")
    public ResponseEntity<ApiResponse> editorUserList(@RequestBody EditorUserListRequestDto editorUserListRequestDto){
        EditorUserListResponseDto editorUserListResponseDto = editorService.editorUserList(editorUserListRequestDto);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("공유 해준 유저 리스트")
                .status(OK.value())
                .data(editorUserListResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> editorUpload(@RequestParam("multipartFile")MultipartFile multipartFile){

        EditorUploadResponseDto editorUploadResponseDto = editorService.editorUpload(multipartFile);

        ApiResponse apiResponse = ApiResponse.builder()
                .message("s3 업로드")
                .status(OK.value())
                .data(editorUploadResponseDto)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
