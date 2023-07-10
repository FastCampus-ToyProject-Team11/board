package com.fastcampus.board.user.dto;

import com.fastcampus.board.user.User;
import lombok.Builder;
import lombok.Getter;

public class UserResponse {

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
