package com.surf.member.controller;

import com.surf.member.authenticator.UserPkAuthenticator;
import com.surf.member.dto.UserInfoRequestDto;
import com.surf.member.dto.UserInfoResponseDto;
import com.surf.member.entity.User;
import com.surf.member.service.UserInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class UserInformationController {

    private final UserInformationService userInformationService;
    private final UserPkAuthenticator userPkAuthentication;

    @PostMapping("/user-info")
    private ResponseEntity<UserInfoResponseDto> userInformationController (@RequestBody UserInfoRequestDto userInfoRequest) {

        int userPk = userInfoRequest.getUserPk();

        if (userPkAuthentication.userPkAuthentication(userPk)) {

            return ResponseEntity.ok(userInformationService.sendUserInformation(userPk));

        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(userInformationService.userNotFound());
        }
    }
}
