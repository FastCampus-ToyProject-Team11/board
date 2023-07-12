package com.fastcampus.board.__core.errors.exception;

import com.fastcampus.board.__core.util.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * not found exception
 */
@Getter
public class Exception404 extends RuntimeException {

    public Exception404(String message) {
        super(message);
    }

    public ApiResponse.Result<Object> body() {
        return ApiResponse.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}