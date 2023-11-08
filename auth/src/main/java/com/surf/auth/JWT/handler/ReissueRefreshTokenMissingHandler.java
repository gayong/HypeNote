package com.surf.auth.JWT.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReissueRefreshTokenMissingHandler {

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<String> handleMissingRefreshToken() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh Token이 없습니다.");
    }
}