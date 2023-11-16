package com.surf.editor.feign.client;

import com.surf.editor.feign.dto.QuizEditorSaveRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "QuizClient",url = "https://k9e101.p.ssafy.io/api/quiz")
public interface QuizOpenFeign {
    @PostMapping(value = "/editor")
    void QuizEditorSave(QuizEditorSaveRequestDto quizEditorSaveRequestDto);
}
