package com.fastcampus.board.__core.errors.exception;

import com.fastcampus.board.__core.errors.ErrorMessage;

public class DuplicateNickNameException extends RuntimeException {

    public DuplicateNickNameException() {
        super(ErrorMessage.DUPLICATE_NICKNAME);
    }
}
