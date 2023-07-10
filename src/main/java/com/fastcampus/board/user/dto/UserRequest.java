package com.fastcampus.board.user.dto;

import com.fastcampus.board.user.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRequest {

    @Getter
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
            String password = passwordEncoder.encode(this.password);
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .nickName(nickName)
                    .build();
        }
    }
}
