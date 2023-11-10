package com.surf.auth.member.service;


import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.repository.UserRepository;
import com.surf.auth.member.authenticator.UserPkAuthenticator;
import com.surf.auth.member.dto.DocumentShareRequestDto;
import com.surf.auth.member.entity.DocumentSharedUser;
import com.surf.auth.member.repository.DocumentSharedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DocumentShareService {

    private final UserRepository userRepository;
    private final UserPkAuthenticator userPkAuthenticator;

    public void saveDocumentShare(DocumentShareRequestDto documentShareRequestDto) {

        Optional<User> userOptional = userRepository.findByUserPk(documentShareRequestDto.getUserPk());

        User user = userOptional.orElseThrow();

        user.getSharedDocumentsRoots().add(documentShareRequestDto.getEditorId());

        userRepository.save(user);

    }
}
