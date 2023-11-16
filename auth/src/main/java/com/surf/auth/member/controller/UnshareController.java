package com.surf.auth.member.controller;

import com.surf.auth.member.authenticator.UserPkAuthenticator;
import com.surf.auth.member.dto.request.UnshareRequestDto;
import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import com.surf.auth.member.service.UnshareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UnshareController {

    private final UserRepository userRepository;
    private final UnshareService unshareService;
    private final UserPkAuthenticator userPkAuthenticator;

    @DeleteMapping("/unshare")
    public ResponseEntity<String> unshareController(@RequestBody UnshareRequestDto unshareRequestDto) {
        List<Integer> userList = unshareRequestDto.getUserPkList();
        String documentId = unshareRequestDto.getDocumentId();

        for (int userPk : userList) {
            if (userPkAuthenticator.userPkAuthentication(userPk)) {
                Optional<User> optionalUser = userRepository.findByUserPk(userPk);

                User user = optionalUser.orElseThrow();

                unshareService.sharedDocumentDelete(documentId, user);
            }
        }
        return ResponseEntity.ok("성공적으로 삭제했습니다.");
    }
}
