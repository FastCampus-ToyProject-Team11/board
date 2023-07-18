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

        // null, "", " " 모두 허용 안함
        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 100, message = "제목은 100자 이내로 작성주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        @Size(max = 20000, message = "내용은 20,000자 이내로 작성해주세요.")
        private String content;

        private MultipartFile thumbnail;    // save.html -> Controller 파일 담는 용도
        private String originalFileName;    // 원본 파일 이름
        private String storedFileName;      // 서버 저장용 파일 이름
        private int fileAttached;           // 파일 첨부 여부 (첨부:1, 미첨부:0)

    }
}
