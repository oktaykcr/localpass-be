package com.localpass.backend.exception;

public class ExceptionFactory {

    public ExceptionFactory() {
    }

    public static ApiError getApiError(ExceptionEnum exceptionEnum, String... params) {
        return new ApiError(exceptionEnum.getErrorCode(), exceptionEnum.getHttpStatus(), params);
    }

    public static ApiError getApiError(ExceptionEnum exceptionEnum) {
        return new ApiError(exceptionEnum.getErrorCode(), exceptionEnum.getHttpStatus());
    }
}
