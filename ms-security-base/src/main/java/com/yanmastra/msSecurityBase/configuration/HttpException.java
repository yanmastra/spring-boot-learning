package com.yanmastra.msSecurityBase.configuration;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException{
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public HttpException(String message) {
        super(message);
    }
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(String message, int status, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.valueOf(status);
    }

    public HttpException(String message, int status) {
        super(message);
        this.status = HttpStatus.valueOf(status);;
    }

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
