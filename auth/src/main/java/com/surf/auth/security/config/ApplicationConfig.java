package com.surf.auth.security.config;

import com.surf.auth.security.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ApplicationConfig {

    private final UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService UserDetailsService() {
        return (username) -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
    }

}
