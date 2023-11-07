package com.surf.auth.auth.service;


import com.surf.auth.JWT.service.AccessTokenIssueService;
import com.surf.auth.JWT.service.RefreshTokenIssueService;
import com.surf.auth.auth.dto.LogInDto;
import com.surf.auth.auth.dto.TokenDto;
import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final AccessTokenIssueService accessTokenIssueService;
    private final RefreshTokenIssueService refreshTokenIssueService;
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    public boolean authentication (LogInDto loginInfo) {
        Optional<User> userOptional = userRepository.findByEmail(loginInfo.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return bCryptPasswordEncoder.matches(loginInfo.getPassword(), user.getPassword());
        }
        return false;
    }

    public TokenDto sendToken () {
        TokenDto issuedToken = new TokenDto();

        issuedToken.setAccessToken(accessTokenIssueService.accessTokenIssue());
        issuedToken.setRefreshToken(refreshTokenIssueService.refreshTokenIssue());
        issuedToken.setMessage("정상적으로 로그인이 되었습니다.");

        return issuedToken;
    }
}