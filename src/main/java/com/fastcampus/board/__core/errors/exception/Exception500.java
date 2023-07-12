package com.fastcampus.board.__core.errors.exception;

import com.fastcampus.board.__core.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * internal server error exception
 */
@Getter
public class Exception500 extends RuntimeException {

    public Exception500(String message) {
        super(message);
    }

    public ApiResponse.Result<?> body() {
        return ApiResponse.error(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
