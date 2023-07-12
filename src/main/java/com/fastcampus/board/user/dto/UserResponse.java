package com.fastcampus.board.user.dto;

import com.fastcampus.board.user.User;
import lombok.*;

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

    @Getter
    @Builder
    public static class LoginDTOWithJWT {

        private LoginDTO loginDTO;
        private String jwt;

        public static LoginDTOWithJWT from(LoginDTO loginDTO, String jwt) {
            return LoginDTOWithJWT.builder()
                    .loginDTO(loginDTO)
                    .jwt(jwt)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class LoginDTO {

        private String username;
        private String nickName;

        public static LoginDTO from(User user) {
            return LoginDTO.builder()
                    .username(user.getUsername())
                    .nickName(user.getNickName())
                    .build();
        }
    }
}
