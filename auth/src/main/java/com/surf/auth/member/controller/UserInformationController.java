package com.surf.auth.member.controller;


import com.surf.auth.JWT.authenticator.AccessTokenAuthenticator;
import com.surf.auth.JWT.decoder.TokenDecoder;
import com.surf.auth.member.authenticator.UserEmailAuthenticator;
import com.surf.auth.member.authenticator.UserNickNameAuthenticator;
import com.surf.auth.member.dto.UserInfoResponseDto;
import com.surf.auth.member.handler.AccessTokenNotValidationHandler;
import com.surf.auth.member.handler.UserNotFoundExceptionHandler;
import com.surf.auth.member.service.UserInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserInformationController {

    private final UserInformationService userInformationService;
    private final UserEmailAuthenticator userEmailAuthenticator;
    private final UserNickNameAuthenticator userNickNameAuthenticator;
    private final TokenDecoder tokenDecoder;
    private final AccessTokenAuthenticator accessTokenAuthenticator;
    private final UserNotFoundExceptionHandler userNotFoundExceptionHandler;
    private final AccessTokenNotValidationHandler accessTokenNotValidationHandler;

    @GetMapping ("/user-info")
    private ResponseEntity<UserInfoResponseDto> userInformationController (@RequestHeader String accessToken) {

        Map<String, String> request = new HashMap<>();

        if (accessTokenAuthenticator.authenticateToken(accessToken)) {

            request.put("email", tokenDecoder.parsingAccessToken(accessToken));

            if (userEmailAuthenticator.userEmailAuthenticator(request.get("email"))) {

                return ResponseEntity.ok(userInformationService.sendUserInformation(request));

            } else {

                return ResponseEntity.status(HttpStatus.CONFLICT).body(userNotFoundExceptionHandler.userNotFound());
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(accessTokenNotValidationHandler.accessTokenNotValidation());
        }


    }

    @GetMapping("/user-info/{nickName}")
    private ResponseEntity<Boolean> userInformationNickNameController (@PathVariable String nickName) {

        if (userNickNameAuthenticator.userNickNameAuthentication(nickName)) {

            return ResponseEntity.ok(true);
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }
    }
}