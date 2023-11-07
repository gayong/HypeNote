package com.surf.auth.auth.service;

import com.surf.auth.auth.dto.RootDto;
import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class RootSaveService {

    private final UserRepository userRepository;

    public HttpStatus rootSaveService(RootDto root) {

        log.info("루트 받아오기:" + root);
        Optional<User> userOptional = userRepository.findByUserId(root.getUserId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.getDocumentsRoots().add(root.getRoot());

            userRepository.save(user);

            log.info("여기까지 성공");

            return HttpStatus.OK;
        } else {

            return HttpStatus.NOT_FOUND;
        }
    }
}
