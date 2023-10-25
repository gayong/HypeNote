package com.surf.editor.service;

import com.surf.editor.domain.Editor;
import com.surf.editor.repository.EditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class EditorService {
    private final EditorRepository editorRepository;

    public void editorCreate(String userId) {
        editorRepository.save(Editor.toEntity(null,null));
    }
}
