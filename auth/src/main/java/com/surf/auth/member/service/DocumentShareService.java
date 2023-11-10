package com.surf.auth.member.service;


import com.surf.auth.member.dto.DocumentShareRequestDto;
import com.surf.auth.member.entity.DocumentSharedUser;
import com.surf.auth.member.repository.DocumentSharedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DocumentShareService {

    private final DocumentSharedUserRepository documentSharedUserRepository;

    public void saveDocumentShare(DocumentShareRequestDto documentShareRequestDto) {
        documentSharedUserRepository.save(DocumentSharedUser.builder()
                .userPk(documentShareRequestDto.getUserPk())
                .editorId(documentShareRequestDto.getEditorId())
                .sharedNickName(documentShareRequestDto.getSharedNickName())
                .writePermission(false)
                .build());
    }
}
