package com.fastcampus.board.__core.errors;

public class ErrorMessage {

    private ErrorMessage() {
        throw new IllegalArgumentException("Suppress default constructor");
    }


    public static final String UN_AUTHORIZED = "인증되지 않았습니다.";
    public static final String FORBIDDEN = "권한이 없습니다.";

    public static final String EMPTY_DATA_FOR_USER_JOIN = "회원 가입을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_UPDATE = "회원 정보 수정을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_CHECK_NICKNAME = "닉네임 중복 검사를 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_CHECK_USERNAME = "유저네임 중복 검사를 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_USER_CHECK_EMAIL = "이메일 중복 검사를 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_SAVE_COMMENT = "댓글 등록을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_USER_FOR_SAVE_COMMENT = "댓글 동록을 위한 회원 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_BOARD_FOR_SAVE_COMMENT = "댓글 등록을 위한 게시글 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_REPORT_SAVE = "신고 등록을 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_FIND_BOARD = "게시글을 찾기 위한 데이터가 존재하지 않습니다.";
    public static final String EMPTY_DATA_FOR_SAVE_CONTENT = "본문 내용을 입력해주세요.";
    public static final String EMPTY_DATA_FOR_SAVE_TITLE = "제목을 입력해주세요.";


    public static final String INVALID_PASSWORD = "패스워드는 4자에서 15자 사이어야 합니다.";
    public static final String INVALID_NICKNAME = "닉네임은 2자에서 15자 사이어야 합니다.";
    public static final String INVALID_EMAIL = "이메일 형식을 맞춰주세요.";

    public static final String LOGIN_FAILED = "회원 정보가 존재하지 않습니다.";
    public static final String NOT_FOUND_USER_FOR_UPDATE = "회원 정보 수정을 위한 회원 정보가 존재하지 않습니다.";
    public static final String DUPLICATE_NICKNAME = "중복되는 닉네임 입니다.";
    public static final String DUPLICATE_USERNAME = "중복되는 유저네임 입니다.";

    public static final String DUPLICATE_REPORT = "같은 게시글을 여러번 신고할 수 없습니다.";
    public static final String NOT_FOUND_BOARD = "게시글 정보가 존재하지 않습니다.";
    public static final String NOT_FOUND_USER = "유저 정보가 존재하지 않습니다.";
}
