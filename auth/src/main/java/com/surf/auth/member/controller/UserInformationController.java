package com.surf.auth.member.controller;


import com.surf.auth.JWT.decoder.TokenDecoder;
import com.surf.auth.member.authenticator.UserEmailAuthenticator;
import com.surf.auth.member.dto.UserInfoResponseDto;
import com.surf.auth.member.service.UserInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class UserInformationController {

    private final UserInformationService userInformationService;
    private final UserEmailAuthenticator userEmailAuthenticator;
    private final TokenDecoder tokenDecoder;

    @PostMapping("/user-info")
    private ResponseEntity<UserInfoResponseDto> userInformationController (@RequestHeader String accessToken) {

        String email = tokenDecoder.parsingAccessToken(accessToken);

        if (userEmailAuthenticator.userEmailAuthenticator(email)) {

            return ResponseEntity.ok(userInformationService.sendUserInformation(email));

        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(userInformationService.userNotFound());
        }
    }
}
