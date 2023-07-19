package com.fastcampus.board.user;

import com.fastcampus.board.__core.errors.exception.Exception500;

import java.util.Objects;

public enum Role {

    SESAC,
    EXCELLENT;

    public static Role from(String name) {
        for (Role role : Role.values()) {
            if (Objects.equals(role.name(), name)) return role;
        }
        throw new Exception500("권한 매칭 오류");
    }
}
