package com.surf.auth.member.service;


import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import com.surf.auth.member.authenticator.UserPkAuthenticator;
import com.surf.auth.member.dto.request.DocumentShareRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DocumentShareService {

    private final UserRepository userRepository;
    private final UserPkAuthenticator userPkAuthenticator;

    public void saveDocumentShare(DocumentShareRequestDto documentShareRequestDto, Integer userPk) {

        Optional<User> userOptional = userRepository.findByUserPk(userPk);

        User user = userOptional.orElseThrow();

        if (!user.getSharedDocumentsRoots().contains(documentShareRequestDto.getDocumentId())) {
            user.getSharedDocumentsRoots().add(documentShareRequestDto.getDocumentId());
            userRepository.save(user);
        }
    }
}
