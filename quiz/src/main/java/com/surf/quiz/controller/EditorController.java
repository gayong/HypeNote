package com.surf.quiz.controller;


import com.surf.quiz.common.BaseResponse;
import com.surf.quiz.common.BaseResponseStatus;
import com.surf.quiz.dto.diagram.DiagramResponseDto;
import com.surf.quiz.dto.editor.ApiResponse;
import com.surf.quiz.dto.editor.EditorCheckResponse;
import com.surf.quiz.dto.request.EditorRequestDto;
import com.surf.quiz.entity.Editor;
import com.surf.quiz.fegin.DiagramServiceFeignClient;
import com.surf.quiz.fegin.EditorServiceFeignClient;
import com.surf.quiz.repository.EditorRepository;
import com.surf.quiz.service.QuizRoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
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

//    @Autowired
//    private DiagramServiceFeignClient diagramServiceFeignClient;

//    fetchDiagramInfo(1);
    //
//    @Autowired
//    private EditorServiceFeignClient editorServiceFeignClient;
//
//    public void fetchDiagramInfo(int userId) {
//        BaseResponse<DiagramResponseDto> response = diagramServiceFeignClient.getNodes(userId);
////        ApiResponse<EditorCheckResponse> response11 = editorServiceFeignClient.getEditor("65392c40cbd1ff6e316819e1");
//        System.out.println("response = " + response.getResult().getLinks());
////        System.out.println("response11 = " + response11.getData().getId());
//    }

}
