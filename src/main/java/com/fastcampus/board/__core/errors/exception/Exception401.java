package com.fastcampus.board.__core.errors.exception;

import com.fastcampus.board.__core.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Authentication Failed exception
 */
@Getter
public class Exception401 extends RuntimeException {

    public Exception401(String message) {
        super(message);
    }

    public ApiResponse.Result<?> body() {
        return ApiResponse.error(getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public HttpStatus status() {
        return HttpStatus.UNAUTHORIZED;
    }
}