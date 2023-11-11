package com.surf.auth.member.service;

import com.surf.auth.auth.dto.rquest.RootDto;
import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserRootDocumentIdSaveService {

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
}
