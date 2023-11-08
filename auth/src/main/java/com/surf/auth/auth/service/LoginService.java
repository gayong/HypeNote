package com.surf.auth.auth.service;


import com.surf.auth.JWT.service.AccessTokenIssueService;
import com.surf.auth.JWT.service.RefreshTokenIssueService;
import com.surf.auth.auth.dto.AuthenticationResultDto;
import com.surf.auth.auth.dto.LogInDto;
import com.surf.auth.auth.dto.TokenDto;
import com.surf.auth.auth.dto.UserDto;
import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
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

            UserDto userInfo = new UserDto();
            userInfo.setUserPk(user.getUserPk());
            userInfo.setEmail(user.getEmail());
            userInfo.setNickName(user.getNickName());
            userInfo.setProfileImage(user.getProfileImage());

            if (bCryptPasswordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
                authenticationResultDto.setResult(true);
                authenticationResultDto.setUserInfo(userInfo);
                return authenticationResultDto;
            }
            return authenticationResultDto;
        }
        return authenticationResultDto;
    }

    public TokenDto sendToken (UserDto userInfo, HttpServletResponse response) {
        TokenDto tokenIssueResult = new TokenDto();

        tokenIssueResult.setAccessToken(accessTokenIssueService.accessTokenIssue(userInfo));
        tokenIssueResult.setUserInfo(userInfo);
        refreshTokenIssueService.refreshTokenIssue(userInfo, response);
        tokenIssueResult.setMessage("정상적으로 로그인이 되었습니다.");

        return tokenIssueResult;
    }
}