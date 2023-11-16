package com.surf.search.common.error.exception;

import com.surf.search.common.error.ErrorCode;

public class NotFoundException extends BaseException {
    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode){
        super(errorCode);
        this.errorCode = errorCode;
    }
}
