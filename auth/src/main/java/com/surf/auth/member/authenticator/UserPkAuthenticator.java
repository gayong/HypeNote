package com.surf.auth.member.authenticator;

import com.surf.auth.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserPkAuthenticator {

    private final UserRepository userRepository;

    public boolean userPkAuthentication (int userPk) {
        return userRepository.findByUserPk(userPk).isPresent();
    }

}
