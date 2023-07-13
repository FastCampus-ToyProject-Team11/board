package com.fastcampus.board.__core.errors.exception;

import com.fastcampus.board.__core.errors.ErrorMessage;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException() {
        super(ErrorMessage.DUPLICATE_USERNAME);
    }
}
