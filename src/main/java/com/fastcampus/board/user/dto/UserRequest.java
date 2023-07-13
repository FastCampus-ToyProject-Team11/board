package com.fastcampus.board.user.dto;

import com.fastcampus.board.user.User;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UserRequest {

    private UserRequest() {
        throw new IllegalArgumentException("Suppress default constructor");
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class JoinDTO {

        @NotBlank
        @Size(min = 2, max = 15)
        private String username;

        @NotBlank
        @Size(min = 4, max = 15)
        private String password;

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        private String email;

        @NotBlank
        @Size(min = 2, max = 15)
        private String nickName;

        public User toEntityWithHashPassword(PasswordEncoder passwordEncoder) {
            String encodedPassword = passwordEncoder.encode(this.password);
            return User.builder()
                    .username(username)
                    .password(encodedPassword)
                    .email(email)
                    .nickName(nickName)
                    .build();
        }
    }

    @Getter
    @ToString
    public static class LoginDTO {

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class UpdateDTO {

        @NotBlank
        private String username;

        @NotBlank
        @Size(min = 4, max = 15)
        private String password;

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
        private String email;

        @NotBlank
        @Size(min = 2, max = 15)
        private String nickName;
    }

    @Getter
    @ToString
    public static class CheckNickNameDTO {

        @NotBlank
        @Size(min = 2, max = 15)
        private String nickName;
    }
}
