package com.surf.auth.auth.controller;

import com.surf.auth.auth.S3.ProfileImageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class Testing {
    private final ProfileImageHandler profileImageHandler;

    @PostMapping("/profile")
    public String SignUp(@RequestPart MultipartFile image) throws IOException {
        log.info("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        log.info(String.valueOf(image.isEmpty()));
        return profileImageHandler.saveFile(image);
    }
}