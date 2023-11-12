package com.surf.auth.member.controller;


import com.surf.auth.JWT.authenticator.AccessTokenAuthenticator;
import com.surf.auth.JWT.decoder.TokenDecoder;
import com.surf.auth.member.authenticator.UserEmailAuthenticator;
import com.surf.auth.member.dto.request.FindUserPkListDto;
import com.surf.auth.member.dto.response.UserInfoResponseDto;
import com.surf.auth.member.dto.response.UserPkResponseDto;
import com.surf.auth.member.handler.FindMemberAccessTokenNotValidationHandler;
import com.surf.auth.member.handler.UserNotFoundExceptionHandler;
import com.surf.auth.member.service.UserInformationNickNameService;
import com.surf.auth.member.service.UserInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserInformationController {

    private final UserInformationService userInformationService;
    private final UserEmailAuthenticator userEmailAuthenticator;
    private final TokenDecoder tokenDecoder;
    private final AccessTokenAuthenticator accessTokenAuthenticator;
    private final UserNotFoundExceptionHandler userNotFoundExceptionHandler;
    private final FindMemberAccessTokenNotValidationHandler findMemberAccessTokenNotValidationHandler;
    private final UserInformationNickNameService userInformationNickNameService;

    @GetMapping ("/user-info")
    private ResponseEntity<UserInfoResponseDto> userInformationController (@RequestHeader("Authorization") String accessTokenHeader) {

        String accessToken = accessTokenHeader.substring(7);

        if (accessTokenAuthenticator.authenticateToken(accessToken)) {

            String email = tokenDecoder.parsingAccessToken(accessToken);

            if (userEmailAuthenticator.userEmailAuthenticator(email)) {

                return ResponseEntity.ok(userInformationService.sendUserInformation(email));

            } else {

                return ResponseEntity.status(HttpStatus.CONFLICT).body(userNotFoundExceptionHandler.userNotFound());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(findMemberAccessTokenNotValidationHandler.accessTokenNotValidation());
        }


    }

    @GetMapping("/user-info/userpk/{userPk}")
    public ResponseEntity<UserInfoResponseDto> userInfoByUserPkController(@PathVariable int userPk) {
        return ResponseEntity.ok(userInformationService.sendUserInformationPk(userPk));
    }

    @PostMapping("/user-info/pk-list")
    public ResponseEntity<List<UserInfoResponseDto>> userInfoByUserPkListController(@RequestBody FindUserPkListDto userPkListDto) {

        List<UserInfoResponseDto> userList = new ArrayList<>();

        List<Integer> userPkList = userPkListDto.getUserPkList();

        for (int userPk : userPkList) {

            userList.add(userInformationService.sendUserInformationPk(userPk));

        }
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/user-info/{nickName}")
    private ResponseEntity<UserPkResponseDto> userInformationNickNameController (@PathVariable String nickName) {

        UserPkResponseDto userPkResponseDto = userInformationNickNameService.findUserPkByNickName(nickName);
        String message = userPkResponseDto.getMessage();
        if (Objects.equals(message, "유저 PK를 성공적으로 반환했습니다.")) {
            return ResponseEntity.ok(userPkResponseDto);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(userPkResponseDto);
        }
    }
}