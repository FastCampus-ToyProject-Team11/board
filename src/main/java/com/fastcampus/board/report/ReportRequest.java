package com.fastcampus.board.report;

import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.user.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class ReportRequest {

    private ReportRequest() {
        throw new IllegalArgumentException("Suppress default constructor");
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class saveDTO {

        @NotBlank
        private Reason reason;

        @NotBlank
        private Long boardId;

        public Report toEntityWith(User user) {
            return Report.builder()
                    .reason(reason)
                    .board(Board.builder().id(boardId).build())
                    .user(user)
                    .build();
        }
    }
}
