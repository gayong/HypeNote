package com.surf.diagram.diagram.controller;


import com.surf.diagram.diagram.common.BaseResponse;
import com.surf.diagram.diagram.common.BaseResponseStatus;
import com.surf.diagram.diagram.dto.request.EditorRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EditorController {

    @PostMapping("/editor")
    @Operation(summary = "에디터 받기")
    public BaseResponse<Void> getEditor(@RequestBody EditorRequestDto editorDto) {
        System.out.println("editorDto = " + editorDto.getEditorId());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
