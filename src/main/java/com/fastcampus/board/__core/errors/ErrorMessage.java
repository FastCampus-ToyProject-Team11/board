package com.fastcampus.board.__core.errors;

public class ErrorMessage {

    private ErrorMessage() {
        throw new IllegalArgumentException("Suppress default constructor");
    }



    public static final String UN_AUTHORIZED = "인증되지 않았습니다.";
    public static final String FORBIDDEN = "권한이 없습니다.";

    public static final String TOKEN_UN_AUTHORIZED = "검증되지 않은 토큰입니다.";
    public static final String TOKEN_EXPIRED = "만료된 토큰입니다.";
    public static final String TOKEN_VERIFICATION_FAILED = "토큰 검증에 실패했습니다.";

    public static final String EMPTY_DATA_FOR_USER_JOIN = "회원 가입을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_LOGIN = "로그인을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_UPDATE = "회원 정보 수정을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_CHECK_NICKNAME = "닉네임 중복 검사를 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_CHECK_USERNAME = "유저네임 중복 검사를 위한 데이터가 존재하지 않습니다.";

    public static final String LOGIN_FAILED = "회원 정보가 존재하지 않습니다.";
    public static final String NOT_FOUND_USER_FOR_UPDATE = "회원 정보 수정을 위한 회원 정보가 존재하지 않습니다.";
    public static final String DUPLICATE_NICKNAME = "중복되는 닉네임 입니다.";
    public static final String DUPLICATE_USERNAME = "중복되는 유저네임 입니다.";
}
