package com.surf.auth.JWT.controller;

import com.surf.auth.JWT.service.ReissueService;
import com.surf.auth.auth.dto.TokenDto;
import com.surf.auth.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody String refreshToken) {

        User userInfo = reissueService.parsingRefreshToken(refreshToken);

        String storedRefreshToken = reissueService.findRefreshTokenByUserPk(String.valueOf(userInfo.getUserPk()));

        TokenDto fail = new TokenDto();

        if ( storedRefreshToken != null) {
            boolean authenticationResult = reissueService.refreshTokenAuthentication(refreshToken, storedRefreshToken);
            if (authenticationResult) {

                return ResponseEntity.ok(reissueService.reissueAccessToken(userInfo));
            } else {

                fail.setMessage("유효하지 않은 토큰입니다.");

                return ResponseEntity.status(HttpStatus.CONFLICT).body(fail);
            }
        } else {

            fail.setMessage("해당 유저가 가진 RefreshToken이 없습니다.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(fail);
        }
    }
}