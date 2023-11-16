package com.surf.auth.member.controller;

import com.surf.auth.member.authenticator.UserPkAuthenticator;
import com.surf.auth.member.dto.request.DocumentShareRequestDto;
import com.surf.auth.member.service.DocumentShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class DocumentShareController {

    private final UserPkAuthenticator userPkAuthenticator;
    private final DocumentShareService documentShareService;

    @PutMapping("/share")
    private ResponseEntity<String> documentShareController (@RequestBody DocumentShareRequestDto documentShareRequestDto) {

        List<Integer> userPkList = documentShareRequestDto.getUserPkList();

        for (Integer userPk : userPkList) {
            if (userPkAuthenticator.userPkAuthentication(userPk)){
                documentShareService.saveDocumentShare(documentShareRequestDto, userPk);
            }
        }
        return ResponseEntity.ok("성공적으로 공유되었습니다.");
    }
}
