package com.surf.auth.member.authenticator;


import com.surf.auth.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserNickNameAuthenticator {

    private final UserRepository userRepository;

    public boolean userNickNameAuthentication (String nickName) {

        return userRepository.findByNickName(nickName).isPresent();
    }

}
