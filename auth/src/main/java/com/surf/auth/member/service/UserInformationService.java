package com.surf.auth.member.service;

import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.repository.UserRepository;
import com.surf.auth.member.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserInformationService {

    private final UserRepository userRepository;

    public UserInfoResponseDto sendUserInformation (String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User userInfo = optionalUser.orElseThrow();

        return UserInfoResponseDto.builder()
                .message("성공적으로 유저 정보를 반환했습니다.")
                .userPk(userInfo.getUserPk())
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickName())
                .profileImage(userInfo.getProfileImage())
                .documentsRoots(userInfo.getDocumentsRoots())
                .role(userInfo.getRole())
                .build();
    }

    public UserInfoResponseDto userNotFound () {

        return UserInfoResponseDto.builder()
                .message("해당 PK 값을 가진 유저가 존재하지 않습니다.")
                .build();
    }

}
