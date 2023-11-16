package com.surf.member.controller;

import com.surf.member.authenticator.UserPkAuthenticator;
import com.surf.member.dto.DocumentShareRequestDto;
import com.surf.member.service.DocumentShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class DocumentShareController {

    private final UserPkAuthenticator userPkAuthenticator;
    private final DocumentShareService documentShareService;

    @PostMapping("/share")
    private ResponseEntity<String> documentShareController (@RequestBody DocumentShareRequestDto documentShareRequestDto) {

        int userPk = documentShareRequestDto.getUserPk();
        if (userPkAuthenticator.userPkAuthentication(userPk)){
            documentShareService.saveDocumentShare(documentShareRequestDto);

            return ResponseEntity.ok("성공적으로 공유되었습니다.");
        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).body("해당 유저가 존재하지 않습니다.");
        }
    }
}
