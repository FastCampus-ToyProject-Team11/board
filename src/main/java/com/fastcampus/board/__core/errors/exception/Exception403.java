package com.fastcampus.board.__core.errors.exception;

import com.fastcampus.board.__core.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Authorization failed exception
 */
@Getter
public class Exception403 extends RuntimeException {

    public Exception403(String message) {
        super(message);
    }

    public ApiResponse.Result<?> body() {
        return ApiResponse.error(getMessage(), HttpStatus.FORBIDDEN);
    }

    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}