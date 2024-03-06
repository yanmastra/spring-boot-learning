package com.yanmastra.microservices.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Primary
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LogManager.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Object> handleAll(Exception ex, WebRequest request) throws Exception {
        log.error(request.getContextPath()+":"+ex.getMessage());
        ResponseEntity<Object> response = super.handleException(ex, request);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        Map<String, Object> newBody = Map.of(
                "success", false,
                "message", ex.getCause() == null ? ex.getMessage():ex.getCause().getMessage(),
                "data", body
        );
        return ResponseEntity.status(response.getStatusCode()).body(newBody);
    }
}
