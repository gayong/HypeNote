package com.surf.editor.common.error;

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
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION(400,"올바르지 않은 형식입니다."),
    FAIL_UPLOAD(400,"파일 업로드에 실패 했습니다."),

    USER_NOT_FOUND(404,"해당 유저를 찾을 수 없습니다."),
    EDITOR_NOT_FOUND(404, "해당 게시글을 찾을 수 없습니다."),

    FAIL_CREATE_EDITOR(400,"문서 생성에 실패 했습니다."),
    FAIL_WRITE_EDITOR(400,"문서 작성에 실패 했습니다."),
    FAIL_DELETE_EDITOR(400,"문서 삭제에 실패 했습니다."),
    FAIL_SEARCH_EDITOR(400,"문서 검색에 실패 했습니다."),

    DIAGRAM_SAVE_FAIL(400,"다이어그램에 문서 정보 저장 실패했습니다"),
    MEMBER_SAVE_FAIL(400,"유저에 문서 정보 저장 실패했습니다."),
    QUIZ_SAVE_FAIL(400,"퀴즈에 문서 정보 저장 실패했습니다."),
    DOCUMENT_DELETE_FAIL(400,"문서 삭제에 실패했습니다"),

    WRITER_PERMISSION_FAIL(400,"문서 쓰기 권한 설정에 실패 했습니다"),

    REDIS_SAVE_FAIL(400,"Redis 저장에 실패했습니다."),
    REDIS_GET_FAIL(400,"Redis 조회에 실패했습니다")
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
