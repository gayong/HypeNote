package com.surf.diagram.diagram.service;


import com.surf.diagram.diagram.dto.member.UserInfoResponseDto;
import com.surf.diagram.diagram.fegin.MemberServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class FeginUserService {
    private final MemberServiceFeignClient memberServiceFeignClient;

    public UserInfoResponseDto userInfoByUserPk(int userPk) {
        UserInfoResponseDto response = memberServiceFeignClient.userInfoByUserPk(userPk);
        System.out.println("response = " + response.getDocumentsRoots());
        return response;
    }

    public UserInfoResponseDto userInfoByToken(String token) {
        UserInfoResponseDto response = memberServiceFeignClient.userInfoByToken(token);
        System.out.println("response = " + response.getDocumentsRoots());
        return response;
    }
}

