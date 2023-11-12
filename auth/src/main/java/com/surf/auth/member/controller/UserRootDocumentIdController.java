package com.surf.auth.member.controller;


import com.surf.auth.member.dto.request.RootDto;
import com.surf.auth.member.dto.request.RootsDto;
import com.surf.auth.member.feign.FeignClientInterface;
import com.surf.auth.member.service.UserRootDocumentIdService;
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
public class UserRootDocumentIdController {

    private final UserRootDocumentIdService userRootDocumentIdService;
    private final FeignClientInterface feignClientInterface;

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
}
