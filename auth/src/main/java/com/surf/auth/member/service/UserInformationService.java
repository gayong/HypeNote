package com.surf.auth.member.service;

import com.surf.auth.auth.entity.User;
import com.surf.auth.auth.repository.UserRepository;
import com.surf.auth.member.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserInformationService {

    private final UserRepository userRepository;

    public UserInfoResponseDto sendUserInformation (Map<String, String> request) {

        Optional<User> optionalUser;
        if (request.get("email") != null){
            optionalUser = userRepository.findByEmail(request.get("email"));
        } else {
            optionalUser = userRepository.findByNickName(request.get("nickName"));
        }

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

}
