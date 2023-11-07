package com.surf.auth.JWT.controller;

import com.surf.auth.JWT.service.ReissueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public void reissue() {
        reissueService.reissueService();
    }
}