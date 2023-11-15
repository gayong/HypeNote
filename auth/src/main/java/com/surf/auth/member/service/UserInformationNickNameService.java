package com.surf.auth.member.service;

import com.surf.auth.member.dto.response.UserPkResponseDto;
import com.surf.auth.member.entity.User;
import com.surf.auth.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserInformationNickNameService {

    private final UserRepository userRepository;

    public UserPkResponseDto findUserPkByNickName (String nickName) {

        Optional<User> optionalUser = userRepository.findByNickName(nickName);

        if (optionalUser.isPresent()) {
            User userInfo = optionalUser.get();
            return UserPkResponseDto.builder()
                    .message("유저 PK를 성공적으로 반환했습니다.")
                    .data(UserPkResponseDto.Data.builder()
                            .userPk(userInfo.getUserPk())
                            .nickName(userInfo.getNickName())
                            .profileImage(userInfo.getProfileImage()).build())
                    .build();
        } else {
            return UserPkResponseDto.builder()
                    .message("해당 닉네임의 유저가 존재하지 않습니다.")
                    .build();
        }
    }
}
