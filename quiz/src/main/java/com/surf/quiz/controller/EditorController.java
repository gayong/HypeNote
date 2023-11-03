package com.surf.quiz.controller;


import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.common.BaseResponseStatus;
import com.surf.quiz.dto.request.EditorRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class EditorController {

    @PostMapping("/editor")
    @Operation(summary = "에디터 받기")
    public BaseResponse<Void> getEditor(@RequestBody EditorRequestDto editorDto) {
        System.out.println("editorDto = " + editorDto.getEditorId());

        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }
}
