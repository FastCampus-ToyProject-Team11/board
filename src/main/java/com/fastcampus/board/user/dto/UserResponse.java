package com.fastcampus.board.user.dto;

import com.fastcampus.board.user.User;
import lombok.*;

public class UserResponse {

    private UserResponse() {
        throw new IllegalArgumentException("Suppress default constructor");
    }

    @Getter
    @Builder
    public static class JoinDTO {

        private String username;
        private String nickName;

        public static JoinDTO from(User user) {
            return JoinDTO.builder()
                    .username(user.getUsername())
                    .nickName(user.getNickName())
                    .build();
        }
    }
}
