package com.surf.auth.member.controller;


import com.surf.auth.member.dto.request.RootDto;
import com.surf.auth.member.service.UserRootDocumentIdSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserRootDocumentIdSaveController {

    private final UserRootDocumentIdSaveService userRootDocumentIdSaveService;

    @PutMapping("/root-save")
    public HttpStatus LogIn(@RequestBody RootDto rootDto) {
        
        log.info("컨트롤러");

        return userRootDocumentIdSaveService.rootSaveService(rootDto);
    }
}
