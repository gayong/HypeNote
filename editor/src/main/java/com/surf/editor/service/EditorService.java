package com.surf.editor.service;

import com.surf.editor.common.error.ErrorCode;
import com.surf.editor.common.error.exception.BaseException;
import com.surf.editor.common.error.exception.NotFoundException;
import com.surf.editor.domain.Editor;
import com.surf.editor.dto.request.EditorHyperLinkRequestDto;
import com.surf.editor.dto.request.EditorRelationRequestDto;
import com.surf.editor.dto.request.EditorShareRequestDto;
import com.surf.editor.dto.request.EditorWriteRequestDto;
import com.surf.editor.dto.response.EditorCheckResponseDto;
import com.surf.editor.dto.response.EditorCreateResponseDto;
import com.surf.editor.dto.response.EditorSearchResponseDto;
import com.surf.editor.feign.client.DiagramOpenFeign;
import com.surf.editor.feign.client.MemberOpenFeign;
import com.surf.editor.feign.client.MemberShareOpenFeign;
import com.surf.editor.feign.client.QuizOpenFeign;
import com.surf.editor.feign.dto.MemberEditorSaveRequestDto;
import com.surf.editor.feign.dto.MemberShareRequestDto;
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
    private final MemberShareOpenFeign memberShareOpenFeign;

    @Transactional
    public EditorCreateResponseDto editorCreate(int userId) {
        Editor savedEditor = new Editor();
        try{
            /**
             * 새로 만들면 부모가 null, 자식도 새로 만들어진다.
             */
            savedEditor = editorRepository.save(Editor.editorCreate(userId));
        }catch (Exception e){
            throw new BaseException(ErrorCode.FAIL_CREATE_EDITOR);
        }

        //feign member, quiz, diagram DB에도 저장
        feign(userId, savedEditor);

        EditorCreateResponseDto editorCreateResponseDto = EditorCreateResponseDto.builder()
                .id(savedEditor.getId())
                .build();

        return editorCreateResponseDto;

    }

    private void feign(int userId, Editor savedEditor) {
        try{
            //루트 게시글 찾기
//            List<String> byParentIdAndUserId = editorRepository.findByParentIdAndUserId(null, userId).orElse(null);
//            Editor findEditor = savedEditor;
//            Editor parentEditor = new Editor();
//            while (true){
//                parentEditor = editorRepository.findByParentId(findEditor.getParentId()).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));
//
//                if(parentEditor.equals(null)){
//                    break;
//                }
//
//                findEditor = parentEditor;
//            }

            MemberEditorSaveRequestDto memberEditorSaveRequestDto = MemberEditorSaveRequestDto.builder()
                    .userId(userId)
                    .root(savedEditor.getId()) //추가 필요
                    .build();

            memberOpenFeign.MemberEditorSave(memberEditorSaveRequestDto);
        }catch (Exception e){
            throw new BaseException(ErrorCode.MEMBER_SAVE_FAIL);
        }

//        try{
//            DiagramEditorSaveRequestDto diagramEditorSaveRequestDto = DiagramEditorSaveRequestDto.builder()
//                    .userId(userId)
//                    .documentId(savedEditor.getId())
//                    .title(savedEditor.getTitle())
//                    .content(savedEditor.getContent())
//                    .build();
//
//            diagramOpenFeign.DiagramEditorSave(diagramEditorSaveRequestDto);
//        }catch (Exception e){
//            throw new BaseException(ErrorCode.DIAGRAM_SAVE_FAIL);
//        }

//        try{
//            QuizEditorSaveRequestDto quizEditorSaveRequestDto = QuizEditorSaveRequestDto.builder()
//                    .userId(userId)
//                    .documentId(savedEditor.getId())
//                    .build();
//
//            quizOpenFeign.QuizEditorSave(quizEditorSaveRequestDto);
//        }catch (Exception e){
//            throw new BaseException(ErrorCode.QUIZ_SAVE_FAIL);
//        }
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

    /**
     * 유저 : 유저 테이블에 공유 문서 루트 값 저장(루트 노드 저장한 것 처럼) -> 공유 문서 테이블 1:N 관계 만드는거 필요
     * 에디터 : 유저한테 공유 문서 루트 값 전송, 여기서 공유 문서는 부모와 자식 관계가 겹쳐도 그냥 보내준다.
     *
     */
    public void editorShare(EditorShareRequestDto editorShareRequestDto) {
        try{
            Editor editor = editorRepository.findById(editorShareRequestDto.getEditorId()).orElseThrow(
                    ()-> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

            feignShare(editorShareRequestDto.getUserId(), editor);

        }catch (Exception e){
            throw new BaseException(ErrorCode.MEMBER_SHARE_FAIL);
        }
    }

    private void feignShare(int userId, Editor savedEditor) {
        try {
            MemberShareRequestDto memberShareRequestDto = MemberShareRequestDto.builder()
                    .userId(userId)
                    .root(savedEditor.getId()) //추가 필요
                    .build();

            memberShareOpenFeign.MemberShare(memberShareRequestDto);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.MEMBER_SHARE_FAIL);
        }
    }

    /**
     *  에디터 쓰기 권한
     *  에디터 별로 쓰기 리스트 생성한 후 쓰기 설정하면 유저 id를 넣어주고 아니면 빼준다.
     *  유저가 해당 에디터에 접속할 때 에디터 상세정보에서 쓰기 권한 true,false를 리턴해준다.
     */
}
