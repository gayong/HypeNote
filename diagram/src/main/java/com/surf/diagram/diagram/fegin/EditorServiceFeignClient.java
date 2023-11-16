package com.surf.diagram.diagram.fegin;



import com.surf.diagram.diagram.dto.editor.ApiResponse;
import com.surf.diagram.diagram.dto.editor.EditorCheckResponse;
import com.surf.diagram.diagram.dto.editor.EditorListRequestDto;
import com.surf.diagram.diagram.dto.editor.EditorListResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name="server-editor", url="https://k9e101.p.ssafy.io")
public interface EditorServiceFeignClient {
    // root 보내면 에디터 리스트 받기
    @PostMapping("/api/editor/list")
    ApiResponse<List<EditorListResponseDto>> editorList(@RequestBody EditorListRequestDto editorListRequestDto);
}
