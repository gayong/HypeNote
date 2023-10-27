package com.surf.editor.common.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private int status;
    private T data;

    @Builder
    public ApiResponse(String message, Integer status, T data){
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
