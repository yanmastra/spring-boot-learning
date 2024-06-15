package com.yanmastra.msSecurityBase.configuration;

import com.yanmastra.msSecurityBase.Log;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Iterator;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({HttpException.class, AuthenticationException.class, IllegalArgumentException.class, ChangeSetPersister.NotFoundException.class})
    public final ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) throws Exception {
        Log.log.error(request.getContextPath()+":"+ex.getMessage());

        Map<String, Object> newBody = Map.of(
                "success", false,
                "message", ex.getCause() == null ? ex.getMessage():ex.getCause().getMessage(),
                "exception", ex.getCause() == null ? ex.getClass().getName():ex.getCause().getClass().getName()
        );
        HttpStatusCode statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (ex) {
            case IllegalArgumentException illegalArgumentException -> statusCode = HttpStatus.BAD_REQUEST;
            case AuthenticationException authenticationException -> statusCode = HttpStatus.UNAUTHORIZED;
            case OAuth2AuthorizationException oAuth2AuthorizationException -> statusCode = HttpStatus.FORBIDDEN;
            default -> {}
        }
        HttpHeaders headers = new HttpHeaders();
        Iterator<String> headerNames = request.getHeaderNames();
        while (headerNames.hasNext()) {
            String key=headerNames.next();
            headers.add(key, headers.getFirst(key));
        }

        return handleExceptionInternal(ex, newBody,  headers, statusCode, request);
    }


}
