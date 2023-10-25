package com.surf.editor.service;

import com.surf.editor.domain.Editor;
import com.surf.editor.dto.request.EditorWriteRequest;
import com.surf.editor.dto.response.EditorCheckResponse;
import com.surf.editor.repository.EditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EditorService {
    private final EditorRepository editorRepository;

    public void editorCreate(String userId) {
        editorRepository.save(Editor.toEntity(null,null));
    }

    public void editorWrite(String editorId, EditorWriteRequest editorWriteRequest) {
        Optional<Editor> byId = editorRepository.findById(editorId);

        System.out.println(byId.get());

        if(byId.isPresent()){
            byId.get().write(editorWriteRequest.getTitle(),editorWriteRequest.getContent());
            editorRepository.save(byId.get());
        }
    }

    public void editorDelete(String editorId) {
        Optional<Editor> byId = editorRepository.findById(editorId);

        if(byId.isPresent()){
            editorRepository.delete(byId.get());
        }
    }

    public EditorCheckResponse editorCheck(String editorId) {
        Optional<Editor> byId = editorRepository.findById(editorId);

        EditorCheckResponse editorCheckResponse = EditorCheckResponse.builder()
                .id(byId.get().getId())
                .title(byId.get().getTitle())
                .content(byId.get().getContent())
                .build();

        return editorCheckResponse;
    }
}
