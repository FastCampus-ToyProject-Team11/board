package com.fastcampus.board.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class BoardRequest {

    @ToString
    @Getter
    @Setter
    public static class saveDTO {

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "본문 내용을 입력해주세요.")
        private String content;

        private String originalFileName;
        private String storedFileName;

    }

    @Getter
    @Setter
    public static class UpdateDTO {

        private Long id;

        @NotBlank(message = "제목을 입력해주세요.")
        private String title;

        @NotBlank(message = "본문 내용을 입력해주세요.")
        private String content;

    }
}
