package com.yanmastra.msSecurityBase.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanmastra.msSecurityBase.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

@Component
public class KeycloakAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    public KeycloakAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Log.log.error(authException.getMessage(), authException);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        RestResponseHeader restResponseHeader = new RestResponseHeader(
                response.getStatus(),
                authException.getMessage(),
                Stream.of(authException.getStackTrace()).map(StackTraceElement::toString).toList()
        );
        OutputStream responseStream = response.getOutputStream();
        mapper.writeValue(responseStream, restResponseHeader);
        responseStream.flush();
    }
}
