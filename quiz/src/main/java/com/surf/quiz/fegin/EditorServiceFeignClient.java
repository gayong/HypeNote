package com.surf.quiz.fegin;

import com.surf.quiz.dto.editor.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name="server-editor", url="https://k9e101.p.ssafy.io")
public interface EditorServiceFeignClient {
    @GetMapping("/api/editor/{editorId}")
    ApiResponse<EditorCheckResponse> getEditor(@PathVariable String editorId);

    @PostMapping("/api/editor/member")
    ApiResponse<EditorShareMemberResponseDto> getShares(@RequestBody EditorShareMemberRequestDto editorShareMemberRequestDto);

    @PostMapping("/api/editor/shareList")
    ApiResponse<EditorShareListResponseDto> editorShareList(@RequestBody EditorShareListRequestDto editorShareListRequestDto);

}
