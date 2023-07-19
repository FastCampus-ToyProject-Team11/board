package com.fastcampus.board.board.dto;

import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.model.Role;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

public class BoardResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ListDTO {
        private Long id;
        private String title;
        private String content;
        private String thumbnailName;
        private String nickname;
        private Role role;

        private MultipartFile thumbnail;
        private String originalFileName;
        private String storedFileName;
        private int fileAttached;

        public static ListDTO toListDTO(Board board) {
            return ListDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .thumbnailName(board.getThumbnailName())
                    .build();
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    @Builder
    public static class DetailDTO {
        private Long id;
        private String title;
        private String content;
//        private String nickname;
        // comment 리스트 (id, comment, 댓글작성자의 nickname)

        public static DetailDTO toDTO(Board board) {
            return DetailDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
//                    .nickname(board.getUser().getNickName())
                    .build();
        }
    }
}
