package com.surf.editor.service;

import com.surf.editor.common.error.ErrorCode;
import com.surf.editor.common.error.exception.BaseException;
import com.surf.editor.common.error.exception.NotFoundException;
import com.surf.editor.domain.Editor;
import com.surf.editor.dto.request.EditorWriteRequest;
import com.surf.editor.dto.response.EditorCheckResponse;
import com.surf.editor.dto.response.EditorSearchResponse;
import com.surf.editor.feign.client.DiagramOpenFeign;
import com.surf.editor.feign.client.MemberOpenFeign;
import com.surf.editor.feign.client.QuizOpenFeign;
import com.surf.editor.feign.dto.DiagramEditorSaveRequestDto;
import com.surf.editor.feign.dto.MemberEditorSaveRequestDto;
import com.surf.editor.feign.dto.QuizEditorSaveRequestDto;
import com.surf.editor.repository.EditorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EditorService {
    private final EditorRepository editorRepository;
    private final DiagramOpenFeign diagramOpenFeign;
    private final MemberOpenFeign memberOpenFeign;
    private final QuizOpenFeign quizOpenFeign;


    @Transactional
    public void editorCreate(int userId) {
        Editor savedEditor = new Editor();
        try{
            savedEditor = editorRepository.save(Editor.toEntity(null, null));
        }catch (Exception e){
            throw new BaseException(ErrorCode.FAIL_CREATE_EDITOR);
        }

        try{
            //루트 게시글 찾기
            List<String> byParentIdAndUserId = editorRepository.findByParentIdAndUserId(null, userId)
                    .orElse(null);

            MemberEditorSaveRequestDto memberEditorSaveRequestDto = MemberEditorSaveRequestDto.builder()
                    .userId(userId)
                    .documentsRoots(byParentIdAndUserId) //추가 필요
                    .build();

            memberOpenFeign.MemberEditorSave(memberEditorSaveRequestDto);
        }catch (Exception e){
            throw new BaseException(ErrorCode.MEMBER_SAVE_FAIL);
        }

        try{
            DiagramEditorSaveRequestDto diagramEditorSaveRequestDto = DiagramEditorSaveRequestDto.builder()
                    .userId(userId)
                    .documentId(savedEditor.getId())
                    .title(savedEditor.getTitle())
                    .content(savedEditor.getContent())
                    .build();

            diagramOpenFeign.DiagramEditorSave(diagramEditorSaveRequestDto);
        }catch (Exception e){
            throw new BaseException(ErrorCode.DIAGRAM_SAVE_FAIL);
        }

        try{
            QuizEditorSaveRequestDto quizEditorSaveRequestDto = QuizEditorSaveRequestDto.builder()
                    .userId(userId)
                    .documentId(savedEditor.getId())
                    .title(savedEditor.getTitle())
                    .content(savedEditor.getContent())
                    .build();

            quizOpenFeign.QuizEditorSave(quizEditorSaveRequestDto);
        }catch (Exception e){
            throw new BaseException(ErrorCode.QUIZ_SAVE_FAIL);
        }

    }

    public void editorWrite(String editorId, EditorWriteRequest editorWriteRequest) {
        Editor byId = editorRepository.findById(editorId).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        try{
            byId.write(editorWriteRequest.getTitle(),editorWriteRequest.getContent());
            editorRepository.save(byId);
        }catch (Exception e){
            throw new BaseException(ErrorCode.FAIL_WRITE_EDITOR);
        }

    }

    public void editorDelete(String editorId) {
        Editor byId = editorRepository.findById(editorId).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        try{
            editorRepository.delete(byId);
        }catch (Exception e){
            throw new BaseException(ErrorCode.FAIL_DELETE_EDITOR);
        }
    }

    public EditorCheckResponse editorCheck(String editorId) {
        Editor byId = editorRepository.findById(editorId).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        EditorCheckResponse editorCheckResponse = EditorCheckResponse.builder()
                .id(byId.getId())
                .title(byId.getTitle())
                .content(byId.getContent())
                .build();

        return editorCheckResponse;
    }

    public EditorSearchResponse editorSearch(String search) {
        List<Editor> byTitleContainingOrContentContaining =
                editorRepository.findByTitleContainingOrContentContaining(search, search)
                        .orElse(null);

        List<EditorSearchResponse.Editors> editors = new ArrayList<>();

        for (Editor editor : byTitleContainingOrContentContaining) {

            editors.add(
                    EditorSearchResponse.Editors.builder()
                    .id(editor.getId())
                    .title(editor.getTitle())
                    .content(editor.getContent())
                    .build());
        }
        EditorSearchResponse editorList = EditorSearchResponse.builder()
                .notes(editors)
                .build();

        return editorList;
    }
}
