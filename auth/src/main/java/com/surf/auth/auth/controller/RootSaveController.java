package com.surf.auth.auth.controller;


import com.surf.auth.auth.dto.RootDto;
import com.surf.auth.auth.service.RootSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class RootSaveController {

    private final RootSaveService rootSaveService;

    @PutMapping("/root-save")
    public HttpStatus LogIn(@RequestBody RootDto rootDto) {
        return rootSaveService.rootSaveService(rootDto);
    }
}
