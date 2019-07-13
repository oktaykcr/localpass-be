package com.localpass.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ApiError.class)
    public final ResponseEntity<ApiErrorResponse> handleApiExceptions(ApiError apiError) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setTimestamp(new Date());
        apiErrorResponse.setErrorCode(apiError.getMessage());
        apiErrorResponse.setHttpStatus(apiError.getHttpStatus());
        apiErrorResponse.setParams(apiError.getParams());
        return new ResponseEntity<>(apiErrorResponse, apiError.getHttpStatus());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiErrorResponse> handleOtherExcepitons(Exception ex) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setTimestamp(new Date());
        apiErrorResponse.setErrorCode(ExceptionEnum.INTERNAL_SERVER_ERROR.getErrorCode());
        apiErrorResponse.setHttpStatus(ExceptionEnum.INTERNAL_SERVER_ERROR.getHttpStatus());

        return new ResponseEntity<>(apiErrorResponse, ExceptionEnum.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}
