package com.surf.diagram.diagram.service;

import com.surf.diagram.diagram.dto.editor.ApiResponse;
import com.surf.diagram.diagram.dto.editor.EditorListRequestDto;
import com.surf.diagram.diagram.dto.editor.EditorListResponseDto;
import com.surf.diagram.diagram.fegin.EditorServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FeginEditorService {
    private final EditorServiceFeignClient editorServiceFeignClient;

    public List<EditorListResponseDto> editorList(EditorListRequestDto editorListRequestDto) {
        ApiResponse<List<EditorListResponseDto>> response = editorServiceFeignClient.editorList(editorListRequestDto);
        return response.getData();
    }
}
