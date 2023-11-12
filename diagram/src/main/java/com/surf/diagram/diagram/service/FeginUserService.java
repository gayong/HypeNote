package com.surf.diagram.diagram.service;


import com.surf.diagram.diagram.dto.member.UserInfoResponseDto;
import com.surf.diagram.diagram.fegin.MemberServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeginUserService {
    @Autowired
    private MemberServiceFeignClient memberServiceFeignClient;

    public UserInfoResponseDto userInfoByUserPk(int userPk) {
        UserInfoResponseDto response = memberServiceFeignClient.userInfoByUserPk(userPk);
        System.out.println("response = " + response.getDocumentsRoots());
        return response;
    }
}
