package com.surf.editor.service;

import com.surf.editor.common.error.ErrorCode;
import com.surf.editor.common.error.exception.BaseException;
import com.surf.editor.common.error.exception.NotFoundException;
import com.surf.editor.domain.Editor;
import com.surf.editor.dto.request.*;
import com.surf.editor.dto.response.EditorCheckResponseDto;
import com.surf.editor.dto.response.EditorCreateResponseDto;
import com.surf.editor.dto.response.EditorListResponseDto;
import com.surf.editor.dto.response.EditorSearchResponseDto;
import com.surf.editor.feign.client.MemberOpenFeign;
import com.surf.editor.feign.dto.MemberEditorSaveRequestDto;
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
    private final MemberOpenFeign memberOpenFeign;


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
            MemberEditorSaveRequestDto memberEditorSaveRequestDto = MemberEditorSaveRequestDto.builder()
                    .userId(userId)
                    .root(savedEditor.getId()) //추가 필요
                    .build();

            memberOpenFeign.MemberEditorSave(memberEditorSaveRequestDto);
        }catch (Exception e){
            throw new BaseException(ErrorCode.MEMBER_SAVE_FAIL);
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
        Editor findParentEditor = editorRepository.findById(editorRelationRequestDto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        Editor findChildEditor = editorRepository.findById(editorRelationRequestDto.getChildId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        findParentEditor.childRelation(editorRelationRequestDto.getChildId());
        findChildEditor.parentRelation(editorRelationRequestDto.getParentId());

        editorRepository.save(findParentEditor);
        editorRepository.save(findChildEditor);
    }

    // 하이퍼 링크 부모는 바뀌지 않는다.
    public void editorHyperLink(int userId, EditorHyperLinkRequestDto editorHyperLinkRequestDto) {
        // db의 hyperLink 리스트에 추가
        Editor findEditor = editorRepository.findById(editorHyperLinkRequestDto.getParentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        findEditor.childHyperLink(editorHyperLinkRequestDto.getChildId());

        editorRepository.save(findEditor);
    }

    /**
     *  에디터 쓰기 권한
     *  에디터 별로 쓰기 리스트 생성한 후 쓰기 설정하면 유저 id를 넣어주고 아니면 빼준다.
     *  유저가 해당 에디터에 접속할 때 에디터 상세정보에서 쓰기 권한 true,false를 리턴해준다.
     */
    public void editorWriterPermission(EditorWriterPermissionRequestDto editorWriterPermissionRequestDto) {
        Editor editor = editorRepository.findById(editorWriterPermissionRequestDto.getEditorId()).orElseThrow(
                ()-> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));

        try{
            if(editorWriterPermissionRequestDto.isWritePermission()){
                editor.writerPermissionAdd(editorWriterPermissionRequestDto.getSharedId());
            }else{
                if(editor.getWritePermission().contains(editorWriterPermissionRequestDto.getEditorId())){
                    editor.writerPermissionSub(editorWriterPermissionRequestDto.getSharedId());
                }
            }
        }catch (Exception e){
            throw new BaseException(ErrorCode.WRITER_PERMISSION_FAIL);
        }

    }

    public List<EditorListResponseDto> editorList(EditorListRequestDto editorListRequestDto) {

        List<String> rootList = editorListRequestDto.getRootList();

        List<EditorListResponseDto> editorListResponseDtoList = new ArrayList<>();
        for (String root : rootList) {
            Editor editor = editorRepository.findById(root).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));
            EditorListResponseDto treeDto = convertEditorToTreeDto(editor);
            editorListResponseDtoList.add(treeDto);
        }

        return editorListResponseDtoList;
    }

    private EditorListResponseDto convertEditorToTreeDto(Editor editor) {
        EditorListResponseDto editorListResponseDto = new EditorListResponseDto();

        editorListResponseDto.setId(editor.getId());
        editorListResponseDto.setTitle(editor.getTitle());
        editorListResponseDto.setParentId(editor.getParentId());

        List<String> childIds = editor.getChildId();
        if(childIds !=null && !childIds.isEmpty()){
            List<EditorListResponseDto> children = new ArrayList<>();

            for (String childId : childIds) {
                Editor childEditor = editorRepository.findById(childId).orElseThrow(() -> new NotFoundException(ErrorCode.EDITOR_NOT_FOUND));
                EditorListResponseDto childDto = convertEditorToTreeDto(childEditor);
                children.add(childDto);
            }
            editorListResponseDto.setChildren(children);
        }
        return editorListResponseDto;
    }


}
