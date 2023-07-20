package com.fastcampus.board.board.dto;

import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.user.Role;
import com.fastcampus.board.user.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

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
        private User user;

        private MultipartFile thumbnail;
        private String originalFileName;
        private String storedFileName;

        public static ListDTO toListDTO(Board board) {
            return ListDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .thumbnailName(board.getThumbnailName())
                    .user(board.getUser())
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
        private Timestamp createdAt;
        private User user;

        public static DetailDTO from(Board board) {
            return DetailDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .user(board.getUser())
                    .createdAt(board.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserSummary {
        private Long userId;
        private Role userRole;
        private Long postCount;
    }
}
