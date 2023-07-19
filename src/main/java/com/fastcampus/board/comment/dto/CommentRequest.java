package com.fastcampus.board.comment.dto;

import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.comment.Comment;
import com.fastcampus.board.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class CommentRequest {

    private CommentRequest() {
        throw new IllegalArgumentException("Suppress default constructor");
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class saveDTO {

        @NotBlank
        private Long boardId;

        @NotBlank
        private String content;

        public Comment toEntityWithUserAndBoard(User user, Board board) {
            return Comment.builder()
                    .content(content)
                    .user(user)
                    .board(board)
                    .build();
        }
    }
}
