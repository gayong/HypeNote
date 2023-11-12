package com.surf.auth.member.controller;


import com.surf.auth.member.authenticator.UserPkAuthenticator;
import com.surf.auth.member.dto.request.RootDto;
import com.surf.auth.member.dto.request.RootsDto;
import com.surf.auth.member.dto.request.SharedDocumentsListDto;
import com.surf.auth.member.dto.response.UserInfoResponseDto;
import com.surf.auth.member.feign.FeignClientInterface;
import com.surf.auth.member.service.UserInformationService;
import com.surf.auth.member.service.UserRootDocumentIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserRootDocumentIdController {

    private final UserRootDocumentIdService userRootDocumentIdService;
    private final FeignClientInterface feignClientInterface;

    private final UserInformationService userInformationService;

    private final UserPkAuthenticator userPkAuthenticator;

    @PutMapping("/root-save")
    public HttpStatus rootSaveController(@RequestBody RootDto rootDto) {
        
        log.info("컨트롤러");

        return userRootDocumentIdService.rootSaveService(rootDto);
    }

    @DeleteMapping("/root-delete")
    public HttpStatus rootDeleteController(@RequestBody RootsDto rootsDto) {

        feignClientInterface.rootDelete(rootsDto);
        return userRootDocumentIdService.rootDeleteService(rootsDto);
    }

    @GetMapping("/root-user-info/{userPk}")
    public ResponseEntity<List<UserInfoResponseDto>> rootUserInfoController(@PathVariable int userPk) {

        List<String> sharedDocumentsList = userInformationService.sendUserInformationPk(userPk).getSharedDocumentsRoots();
        SharedDocumentsListDto sharedDocumentsListDto = new SharedDocumentsListDto();
        sharedDocumentsListDto.setSharedDocumentsList(sharedDocumentsList);

        List<Integer> userList = feignClientInterface.rootInspect(sharedDocumentsListDto);

        List<UserInfoResponseDto> userInfoList = new ArrayList<>();

        for (int sharedUserPk : userList) {
            if (userPkAuthenticator.userPkAuthentication(sharedUserPk)) {
                userInfoList.add(userInformationService.sendUserInformationPk(sharedUserPk));
            }
        }
        return ResponseEntity.ok(userInfoList);


    }
}
