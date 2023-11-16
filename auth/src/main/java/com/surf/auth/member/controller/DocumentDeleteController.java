package com.surf.auth.member.controller;

import com.surf.auth.member.dto.request.DocumentDeleteDto;
import com.surf.auth.member.service.UserRootDocumentIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class DocumentDeleteController {

    private final UserRootDocumentIdService userRootDocumentIdService;

    @DeleteMapping("/document-delete")
    public HttpStatus documentDeleteController(@RequestBody DocumentDeleteDto documentDeleteDto) {

        return userRootDocumentIdService.sharedRootDeleteService(documentDeleteDto);
    }
}
