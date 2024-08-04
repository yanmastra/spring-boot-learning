package com.yanmastra.msSecurityBase.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yanmastra.msSecurityBase.Log;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

@Component("keycloakAuthenticationEntryPoint")
public class KeycloakAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Value("${logging.level.root:error}")
    String logLevel;

    public KeycloakAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Log.log.error(authException.getMessage(), authException);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        RestResponseHeader restResponseHeader = new RestResponseHeader(
                response.getStatus(),
                authException.getMessage(),
                logLevel.equalsIgnoreCase("debug") ? Stream.of(authException.getStackTrace()).map(StackTraceElement::toString).toList()
                        : null
        );
        OutputStream responseStream = response.getOutputStream();
        mapper.writeValue(responseStream, restResponseHeader);
        responseStream.flush();
    }
}
