package com.surf.auth.auth.controller;

import com.surf.auth.auth.S3.ProfileImageService;
import com.surf.auth.auth.dto.SignUpDto;
import com.surf.auth.auth.service.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class Testing {
    private final ProfileImageService profileImageService;

    @PostMapping("/profile")
    public String SignUp(@RequestPart MultipartFile image) throws IOException {
        log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        log.info(String.valueOf(image.isEmpty()));
        return profileImageService.saveFile(image);
    }
}