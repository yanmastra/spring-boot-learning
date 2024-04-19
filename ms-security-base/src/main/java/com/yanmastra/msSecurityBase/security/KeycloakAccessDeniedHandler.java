package com.yanmastra.msSecurityBase.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanmastra.msSecurityBase.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

@Component
public class KeycloakAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException {
        Log.log.error(authException.getMessage(), authException);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

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
