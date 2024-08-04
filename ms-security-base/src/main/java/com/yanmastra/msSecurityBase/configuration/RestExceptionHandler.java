package com.yanmastra.msSecurityBase.configuration;

import com.yanmastra.msSecurityBase.Log;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            RuntimeException.class,
            ChangeSetPersister.NotFoundException.class,
            IOException.class,
    })
    public final ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) throws Exception {
        Log.log.error(request.getContextPath()+":"+ex.getMessage(), ex);

        HttpStatusCode statusCode = getHttpStatusCode(ex);
        Map<String, Object> newBody = Map.of(
                "success", false,
                "message", ex.getCause() == null ? ex.getMessage():ex.getCause().getMessage(),
                "exception", ex.getCause() == null ? ex.getClass().getName():ex.getCause().getClass().getName(),
                "status", statusCode.value()
        );
        HttpHeaders headers = new HttpHeaders();
        Iterator<String> headerNames = request.getHeaderNames();
        while (headerNames.hasNext()) {
            String key=headerNames.next();
            if (key.toLowerCase().contains("cookie") ||
            key.toLowerCase().contains("token")) {
                headers.add(key, request.getHeader(key));
            }
        }
        return handleExceptionInternal(ex, newBody,  headers, statusCode, request);
    }

    private static HttpStatusCode getHttpStatusCode(Exception ex) {
        return switch (ex) {
            case IllegalArgumentException illegalArgumentException -> HttpStatus.BAD_REQUEST;
            case AuthenticationException authenticationException -> HttpStatus.UNAUTHORIZED;
            case JwtValidationException jwtValidationException -> HttpStatus.UNAUTHORIZED;
            case OAuth2AuthorizationException oAuth2AuthorizationException -> HttpStatus.FORBIDDEN;
            case RestClientResponseException restClientResponseException -> restClientResponseException.getStatusCode();
            case HttpException httpException -> httpException.getStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpException.getStatus();
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }


}
