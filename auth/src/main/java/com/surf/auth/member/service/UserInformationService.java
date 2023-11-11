package com.surf.auth.member.service;

import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import com.surf.auth.member.dto.response.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserInformationService {

    private final UserRepository userRepository;

    public UserInfoResponseDto sendUserInformation (String email) {

        Optional<User> optionalUser;

        optionalUser = userRepository.findByEmail(email);

        User userInfo = optionalUser.orElseThrow();


        return UserInfoResponseDto.builder()
                .message("성공적으로 유저 정보를 반환했습니다.")
                .userPk(userInfo.getUserPk())
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickName())
                .profileImage(userInfo.getProfileImage())
                .documentsRoots(userInfo.getDocumentsRoots())
                .sharedDocumentsRoots(userInfo.getSharedDocumentsRoots())
                .role(userInfo.getRole())
                .build();
    }

    public UserInfoResponseDto sendUserInformationPk (int userPk) {

        Optional<User> optionalUser;

        optionalUser = userRepository.findByUserPk(userPk);

        User userInfo = optionalUser.orElseThrow();


        return UserInfoResponseDto.builder()
                .message("성공적으로 유저 정보를 반환했습니다.")
                .userPk(userInfo.getUserPk())
                .email(userInfo.getEmail())
                .nickName(userInfo.getNickName())
                .profileImage(userInfo.getProfileImage())
                .documentsRoots(userInfo.getDocumentsRoots())
                .sharedDocumentsRoots(userInfo.getSharedDocumentsRoots())
                .role(userInfo.getRole())
                .build();
    }

}
