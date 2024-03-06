package com.yanmastra.microservices.configuration;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException{
    private int status = 500;

    public HttpException(String message) {
        super(message);
    }
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(String message, int status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpException(String message, int status) {
        super(message);
        this.status = status;
    }

    public HttpException(String message, HttpStatus status) {
        super(message);
        this.status = status.value();
    }

    public int getStatus() {
        return status;
    }
}
