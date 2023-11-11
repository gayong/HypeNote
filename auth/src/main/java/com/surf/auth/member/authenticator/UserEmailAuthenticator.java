package com.surf.auth.member.authenticator;

import com.surf.auth.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserEmailAuthenticator {

    private final UserRepository userRepository;

    public boolean userEmailAuthenticator (String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
