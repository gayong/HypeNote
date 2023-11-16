package com.surf.auth.member.service;

import com.surf.auth.member.dto.request.DocumentDeleteDto;
import com.surf.auth.member.dto.request.RootDto;
import com.surf.auth.member.dto.request.RootsDto;
import com.surf.auth.member.entity.User;
import com.surf.auth.member.feign.FeignClientInterface;
import com.surf.auth.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserRootDocumentIdService {

    private final UserRepository userRepository;

    public HttpStatus rootSaveService(RootDto root) {

        Optional<User> userOptional = userRepository.findByUserPk(root.getUserPk());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.getDocumentsRoots().add(root.getRoot());

            userRepository.save(user);

            return HttpStatus.OK;
        } else {

            return HttpStatus.NOT_FOUND;
        }
    }

    public HttpStatus rootDeleteService(RootsDto rootsDto) {
        Optional<User> userOptional = userRepository.findByUserPk(rootsDto.getUserPk());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            for (String documentRoot : rootsDto.getDocumentsIdRoots()) {

                user.getDocumentsRoots().removeIf(root -> root.equals(documentRoot));
                userRepository.save(user);
            }
        } else {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.OK;
    }

    public HttpStatus sharedRootDeleteService(DocumentDeleteDto documentDeleteDto) {

        Map<Integer, String> rootDocument = documentDeleteDto.getRootDocument();

        if (rootDocument != null) {
            int userPk = rootDocument.keySet().iterator().next();
            Optional<User> userOptional = userRepository.findByUserPk(userPk);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.getDocumentsRoots().removeIf(root -> root.equals(rootDocument.get(userPk)));
                userRepository.save(user);
            }
        }

        Map<Integer, Set<String>> sharedDocumentsList = documentDeleteDto.getSharedDocumentsList();

        if (sharedDocumentsList != null) {
            for (Integer userPk : sharedDocumentsList.keySet()) {
                Optional<User> userOptional = userRepository.findByUserPk(userPk);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    for (String documentRoot : sharedDocumentsList.get(userPk)) {
                        user.getSharedDocumentsRoots().removeIf(root -> root.equals(documentRoot));
                        userRepository.save(user);
                    }
                }
            }
        }
        return HttpStatus.OK;
    }
}
