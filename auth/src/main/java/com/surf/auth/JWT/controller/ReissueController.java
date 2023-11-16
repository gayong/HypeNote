package com.surf.auth.JWT.controller;

import com.surf.auth.JWT.decoder.TokenDecoder;
import com.surf.auth.JWT.service.ReissueService;
import com.surf.auth.auth.dto.response.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ReissueController {

    private final ReissueService reissueService;
    private final TokenDecoder tokenDecoder;

    @GetMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@CookieValue(name = "refreshToken", required = false) String refreshToken) {

        String email = tokenDecoder.parsingRefreshToken(refreshToken);

        String storedRefreshToken = reissueService.findRefreshTokenByUserEmail(email);

        TokenDto fail = new TokenDto();

        if (refreshToken == null) {
            fail.setMessage("RefreshToken 쿠키가 없습니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(fail);
        }

        if ( storedRefreshToken != null) {
            boolean authenticationResult = reissueService.refreshTokenAuthentication(refreshToken, storedRefreshToken);
            if (authenticationResult) {

                return ResponseEntity.ok(reissueService.reissueAccessToken(email));
            } else {

                fail.setMessage("유효하지 않은 토큰입니다.");

                return ResponseEntity.status(HttpStatus.CONFLICT).body(fail);
            }
        } else {

            fail.setMessage("존재하지않는 RefreshToken입니다. 다시 로그인 해주세요.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(fail);
        }
    }
}