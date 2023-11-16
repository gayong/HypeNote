package com.surf.quiz.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.surf.quiz.common.BaseResponseStatus.SUCCESS;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result", "status", "errorMessage"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private  Boolean isSuccess;
    private  String message;
    private  int code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorMessage;

    // 요청에 성공한 경우
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.result = result;

        this.status = 0;
        this.errorMessage = null;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }


    public BaseResponse(int status, String errorMessage) {
        this.isSuccess = false;
        this.message = errorMessage;
        this.code = status;
        this.status = status;
        this.errorMessage = errorMessage;
    }
}

