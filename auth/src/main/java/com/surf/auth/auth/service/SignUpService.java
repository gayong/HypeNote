package com.surf.auth.auth.service;

import com.surf.auth.auth.S3.ProfileImageHandler;
import com.surf.auth.auth.dto.rquest.SignUpDto;
import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final ProfileImageHandler profileImageHandler;

    public ResponseEntity<String> saveUser(@Valid SignUpDto signupInfo) throws IOException {

        boolean isEmailDuplicate = userRepository.findByEmail(signupInfo.getEmail()).isPresent();
        boolean isNickNameDuplicate = userRepository.findByNickName(signupInfo.getNickName()).isPresent();

        if (isEmailDuplicate && isNickNameDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일 주소와 닉네임입니다.");
        } else if (isEmailDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일 주소입니다.");
        } else if (isNickNameDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 닉네임입니다.");
        }

        List<String> documentsRoots = new ArrayList<>();

        userRepository.save(User.builder()
                .email(signupInfo.getEmail())
                .password(bCryptPasswordEncoder.encode(signupInfo.getPassword()))
                .nickName(signupInfo.getNickName())
                .role("ROLE_USER")
                .profileImage(profileImageHandler.saveFile(signupInfo.getProfileImage()))
                .documentsRoots(documentsRoots)
                .build());
        return ResponseEntity.status(HttpStatus.OK).body("정상적인 가입이 되었습니다.");
    }
}
