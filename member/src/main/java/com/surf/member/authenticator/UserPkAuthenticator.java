package com.surf.member.authenticator;

import com.surf.member.repository.UserRepository;
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
