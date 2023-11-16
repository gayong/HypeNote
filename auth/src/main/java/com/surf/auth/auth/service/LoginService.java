package com.surf.auth.auth.service;


import com.surf.auth.JWT.issuer.AccessTokenIssuer;
import com.surf.auth.JWT.issuer.RefreshTokenIssuer;
import com.surf.auth.auth.dto.response.AuthenticationResultDto;
import com.surf.auth.auth.dto.rquest.LogInDto;
import com.surf.auth.auth.dto.response.TokenDto;
import com.surf.auth.auth.dto.response.UserDto;
import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final AccessTokenIssuer accessTokenIssuer;
    private final RefreshTokenIssuer refreshTokenIssuer;
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
            userInfo.setDocumentsRoots(user.getDocumentsRoots());
            userInfo.setRole(user.getRole());

            if (bCryptPasswordEncoder.matches(loginInfo.getPassword(), user.getPassword())) {
                authenticationResultDto.setResult(true);
                authenticationResultDto.setUserInfo(userInfo);
                return authenticationResultDto;
            }
            return authenticationResultDto;
        }
        return authenticationResultDto;
    }

    public TokenDto sendToken (String email, HttpServletResponse response) {
        TokenDto tokenIssueResult = new TokenDto();

        tokenIssueResult.setAccessToken(accessTokenIssuer.accessTokenIssue(email));
        refreshTokenIssuer.refreshTokenIssue(email, response);
        tokenIssueResult.setMessage("정상적으로 로그인이 되었습니다.");

        return tokenIssueResult;
    }
}