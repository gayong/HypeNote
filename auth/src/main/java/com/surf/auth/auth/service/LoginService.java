package com.surf.auth.auth.service;


import com.surf.auth.JWT.service.AccessTokenIssueService;
import com.surf.auth.JWT.service.RefreshTokenIssueService;
import com.surf.auth.auth.dto.AuthenticationResultDto;
import com.surf.auth.auth.dto.LogInDto;
import com.surf.auth.auth.dto.TokenDto;
import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

    public AuthenticationResultDto authentication (LogInDto loginInfo) {
        Optional<User> userOptional = userRepository.findByEmail(loginInfo.getEmail());

        AuthenticationResultDto authenticationResultDto = new AuthenticationResultDto();

        if (userOptional.isPresent()) {

            User user = userOptional.get();

            if (bCryptPasswordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
                authenticationResultDto.setResult(true);
                authenticationResultDto.setUserInfo(user);
                return authenticationResultDto;
            }
            return authenticationResultDto;
        }
        return authenticationResultDto;
    }

    public TokenDto sendToken (User userInfo) {
        TokenDto issuedToken = new TokenDto();

        issuedToken.setAccessToken(accessTokenIssueService.accessTokenIssue(userInfo));
        issuedToken.setRefreshToken(refreshTokenIssueService.refreshTokenIssue(userInfo));
        issuedToken.setMessage("정상적으로 로그인이 되었습니다.");

        return issuedToken;
    }
}