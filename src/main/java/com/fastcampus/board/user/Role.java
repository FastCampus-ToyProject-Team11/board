package com.fastcampus.board.user;

public enum Role {
    SESAC("새싹회원"),
    EXCELLENT("우수회원");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
