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
        @Size(max = 100, message = "제목은 100자 이내로 작성주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 20000, message = "내용은 20,000자 이내로 작성해주세요.")
        private String content;

        private MultipartFile thumbnail;
        private String originalFileName;
        private String storedFileName;
        private int fileAttached;

    }

    @Getter
    @Setter
    public static class UpdateDTO {

        private Long id;

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 100, message = "제목은 100자 이내로 작성주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 20000, message = "내용은 20,000자 이내로 작성해주세요.")
        private String content;

    }
}
