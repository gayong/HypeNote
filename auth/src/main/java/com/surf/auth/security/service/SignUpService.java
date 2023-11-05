package com.surf.auth.security.service;

import com.surf.auth.security.dto.SignUpDto;
import com.surf.auth.security.entity.User;
import com.surf.auth.security.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    public String saveUser(@Valid SignUpDto user) {

        boolean isEmailDuplicate = userRepository.findByEmail(user.getEmail()).isPresent();
        boolean isNickNameDuplicate = userRepository.findByNickName(user.getNickName()).isPresent();

        if (isEmailDuplicate && isNickNameDuplicate) {
            return "이미 가입된 이메일 주소와 닉네임입니다.";
        } else if (isEmailDuplicate) {
            return "이미 가입된 이메일 주소입니다.";
        } else if (isNickNameDuplicate) {
            return "이미 사용 중인 닉네임입니다.";
        }

        userRepository.save(User.builder()
                .email(user.getEmail())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .nickName(user.getNickName())
                .role("ROLE_USER")
                .build());
        return "정상적인 가입이 되었습니다.";
    }
}
