package com.surf.quiz.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> handleBaseException(final BaseException e) {
        // BaseException의 status를 사용하여 BaseResponse 생성
        return new BaseResponse<>(e.getStatus());
    }

    // 필요에 따라 다른 Exception들도 처리
    // 예를 들어, IllegalArgumentException을 처리한다면 다음과 같이 작성
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> handleIllegalArgumentException(final IllegalArgumentException e) {
        // IllegalArgumentException 발생 시, BAD_REQUEST 상태와 에러 메시지 반환
        return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> handleNullPointerException(final NullPointerException e) {
        // NullPointerException 발생 시, BAD_REQUEST 상태와 에러 메시지 반환
        return new BaseResponse<>(BaseResponseStatus.REQUEST_ERROR);
    }
}
