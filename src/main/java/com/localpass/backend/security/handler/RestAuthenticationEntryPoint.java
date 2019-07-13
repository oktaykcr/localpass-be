package com.localpass.backend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localpass.backend.exception.ApiErrorResponse;
import com.localpass.backend.exception.ExceptionEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse();
        apiErrorResponse.setHttpStatus(ExceptionEnum.LOGIN_FAILURE.getHttpStatus());
        apiErrorResponse.setTimestamp(new Date());
        apiErrorResponse.setErrorCode(ExceptionEnum.LOGIN_FAILURE.getErrorCode());

        httpServletResponse.setStatus(ExceptionEnum.LOGIN_FAILURE.getHttpStatus().value());
        OutputStream outputStream = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, apiErrorResponse);
        outputStream.flush();
    }
}
