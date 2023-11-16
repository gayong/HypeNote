package com.surf.auth.member.service;


import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UnshareService {

    private final UserRepository userRepository;

    public void sharedDocumentDelete(String documentId, User user) {
        List<String> sharedDocumentsRoots = user.getSharedDocumentsRoots();

        if (sharedDocumentsRoots != null && sharedDocumentsRoots.contains(documentId)) {
            sharedDocumentsRoots.remove(documentId);
            userRepository.save(user);
        }
    }
}
