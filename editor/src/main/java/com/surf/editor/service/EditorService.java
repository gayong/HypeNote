package com.surf.editor.service;

import com.surf.editor.common.error.ErrorCode;
import com.surf.editor.common.error.exception.BaseException;
import com.surf.editor.common.error.exception.NotFoundException;
import com.surf.editor.domain.Editor;
import com.surf.editor.dto.request.EditorHyperLinkRequestDto;
import com.surf.editor.dto.request.EditorRelationRequestDto;
import com.surf.editor.dto.request.EditorWriteRequestDto;
import com.surf.editor.dto.response.EditorCheckResponseDto;
import com.surf.editor.dto.response.EditorCreateResponseDto;
import com.surf.editor.dto.response.EditorSearchResponseDto;
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
    public EditorCreateResponseDto editorCreate(int userId) {
        Editor savedEditor = new Editor();
        try{
            /**
             * 새로 만들면 부모가 null, 자식도 새로 만들어진다.
             */
            savedEditor = editorRepository.save(Editor.editorCreate());
        }catch (Exception e){
            throw new BaseException(ErrorCode.FAIL_CREATE_EDITOR);
        }

        //feign member, quiz, diagram DB에도 저장
//        feign(userId, savedEditor);

        EditorCreateResponseDto editorCreateResponseDto = EditorCreateResponseDto.builder()
                .id(savedEditor.getId())
                .build();

        return editorCreateResponseDto;

    }

    private void feign(int userId, Editor savedEditor) {
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
                    .build();

            quizOpenFeign.QuizEditorSave(quizEditorSaveRequestDto);
        }catch (Exception e){
            throw new BaseException(ErrorCode.QUIZ_SAVE_FAIL);
        }
    }

    public void editorWrite(String editorId, EditorWriteRequestDto editorWriteRequest) {
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

        //부모,자녀 관계 제거
        Editor parentEditor = editorRepository.findById(byId.getParentId()).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));
        parentEditor.childDelete(byId.getId());

        for (String childId : byId.getChildId()) {
            Editor childEditor = editorRepository.findById(childId).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));
            childEditor.parentDelete();
        }

        try{
            editorRepository.delete(byId);
        }catch (Exception e){
            throw new BaseException(ErrorCode.FAIL_DELETE_EDITOR);
        }
    }

    public EditorCheckResponseDto editorCheck(String editorId) {
        Editor byId = editorRepository.findById(editorId).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        EditorCheckResponseDto editorCheckResponse = EditorCheckResponseDto.builder()
                .id(byId.getId())
                .title(byId.getTitle())
                .content(byId.getContent())
                .build();

        return editorCheckResponse;
    }

    public EditorSearchResponseDto editorSearch(String search) {
        List<Editor> byTitleContainingOrContentContaining =
                editorRepository.findByTitleContainingOrContentContaining(search, search)
                        .orElse(null);

        List<EditorSearchResponseDto.Editors> editors = new ArrayList<>();

        for (Editor editor : byTitleContainingOrContentContaining) {

            editors.add(
                    EditorSearchResponseDto.Editors.builder()
                    .id(editor.getId())
                    .title(editor.getTitle())
                    .content(editor.getContent())
                    .build());
        }
        EditorSearchResponseDto editorList = EditorSearchResponseDto.builder()
                .notes(editors)
                .build();

        return editorList;
    }


    public void editorRelation(int userId, EditorRelationRequestDto editorRelationRequestDto) {
        // db의 child 리스트에 추가
        Editor findEditor = editorRepository.findById(editorRelationRequestDto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        findEditor.childRelation(editorRelationRequestDto.getChildId());

        editorRepository.save(findEditor);
    }

    public void editorHyperLink(int userId, EditorHyperLinkRequestDto editorHyperLinkRequestDto) {
        // db의 hyperLink 리스트에 추가
        Editor findEditor = editorRepository.findById(editorHyperLinkRequestDto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        findEditor.childHyperLink(editorHyperLinkRequestDto.getChildId());

        editorRepository.save(findEditor);
    }
}
