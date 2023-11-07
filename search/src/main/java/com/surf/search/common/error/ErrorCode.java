package com.surf.search.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "서버에 문제가 생겼습니다."),
    ILLEGAL_ARGUMENT_EXCEPTION(400,"올바르지 않은 형식의 입력입니다."),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(400,"올바르지 않은 형식입니다.")
    ;

    private int status;
    private String message;

    private static final Map<String, ErrorCode> messageMap = Collections.unmodifiableMap(
            Stream.of(values()).collect(
                    Collectors.toMap(ErrorCode::getMessage, Function.identity())));

    public static ErrorCode fromMessage(String message){
        return messageMap.get(message);
    }
}
