package com.example.securitystudy.auth;

import com.example.securitystudy.auth.model.PrincipalDetails;
import com.example.securitystudy.user.UserProvider;
import com.example.securitystudy.user.UserService;
import com.example.securitystudy.user.entity.User;
import com.example.securitystudy.util.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class PrincipalOAuth2DetailsService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserProvider userProvider;
    private final UserService userService;

    @Autowired
    public PrincipalOAuth2DetailsService(PasswordEncoder passwordEncoder, UserProvider userProvider, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userProvider = userProvider;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String username;
        String nickname;
        String email;
        String password;
        String role = "ROLE_USER";
        String provider_id;

        User user = null;

        if (provider.equals("google")) {
            username = oAuth2User.getAttributes().get("name").toString();
            nickname = oAuth2User.getAttributes().get("name").toString();
            email = oAuth2User.getAttributes().get("email").toString();
            provider_id = oAuth2User.getAttributes().get("sub").toString();
            password = passwordEncoder.encode(email);
        } else if (provider.equals("kakao")) {
            Map<String,Object> kakaoAccount = (Map<String,Object>)oAuth2User.getAttributes().get("kakao_account");
            Map<String,Object> kakaoProfile = (Map<String,Object>)kakaoAccount.get("profile");

            username = kakaoProfile.get("nickname").toString();

            nickname = kakaoProfile.get("nickname").toString();
            email = kakaoAccount.get("email").toString();
            provider_id = oAuth2User.getAttributes().get("id").toString();
            password = passwordEncoder.encode(email);

        } else if (provider.equals("naver")) {
            Map<String,Object> response= (Map<String,Object>)oAuth2User.getAttribute( "response" );

            username=response.get("name").toString();
            nickname=response.get("nickname").toString();
            email=response.get("email").toString();
            provider_id = response.get("id").toString();
            password = passwordEncoder.encode(email);

        } else {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }


        try {
            if ( userProvider.checkEmailProvider(email, provider) == 0) {
                log.info(provider+"로그인이 최초입니다. 회원가입을 진행합니다.");
                user = new User(username, nickname, email, password, role, provider, provider_id, 1);
                userService.createUser(user);
            }else {
                log.info(provider+"로그인 기록이 있습니다.");
                user = userProvider.retrieveByEmailProvider(email, provider);
            }
        } catch (BaseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("user = " + user + oAuth2User.getAttributes());
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
