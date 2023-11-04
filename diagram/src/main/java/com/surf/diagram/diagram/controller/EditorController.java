package com.surf.diagram.diagram.controller;


import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.common.BaseResponseStatus;
import com.surf.diagram.diagram.dto.editor.ApiResponse;
import com.surf.diagram.diagram.dto.editor.EditorCheckResponse;
import com.surf.diagram.diagram.dto.request.EditorRequestDto;
import com.surf.diagram.diagram.entity.Editor;
import com.surf.diagram.diagram.fegin.EditorServiceFeignClient;
import com.surf.diagram.diagram.repository.EditorRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EditorController {
    @Autowired
    private EditorServiceFeignClient editorServiceFeignClient;
    private final EditorRepository editorRepository;

    @PostMapping("/editor")
    @Operation(summary = "에디터 받기")
    public BaseResponse<Void> getEditor(@RequestBody EditorRequestDto editorDto) {
        System.out.println("editorDto = " + editorDto.getEditorId());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }


    @GetMapping("/editor/{userId}")
    @Operation(summary = "에디터 받기")
    public BaseResponse<ApiResponse<EditorCheckResponse>> getEditorInfo(@PathVariable int userId) {
        ApiResponse<EditorCheckResponse> response = editorServiceFeignClient.getEditor("65426205cd1e39028569f167");
        System.out.println("response = " + response.getData().getId());
        Editor editor = new Editor();
        editor.setEditorId(response.getData().getId());
        editor.setUserPk(1);
        editor.setContent("content");
        editor.setTitle("title");
        editorRepository.save(editor);
        return new BaseResponse<>(response);
    }
}
