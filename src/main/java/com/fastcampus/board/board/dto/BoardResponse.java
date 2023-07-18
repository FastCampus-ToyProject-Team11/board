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
        private String nickname;
        private Role role;

        private MultipartFile thumbnail;    // save.html -> Controller 파일 담는 용도
        private String originalFileName;    // 원본 파일 이름
        private String storedFileName;      // 서버 저장용 파일 이름
        private int fileAttached;           // 파일 첨부 여부 (첨부:1, 미첨부:0)

        // entity -> DTO
        public static ListDTO toListDTO(Board board) {
            ListDTO listDTO = new ListDTO();
            listDTO.setId(board.getId());
            listDTO.setTitle(board.getTitle());
            listDTO.setContent(board.getContent());
            listDTO.setFileAttached(board.getFileAttached());

            if (board.getFileAttached() == 1) {
                listDTO.setOriginalFileName(board.getBoardFileList().get(0).getOriginalFileName());
                listDTO.setStoredFileName(board.getBoardFileList().get(0).getStoredFileName());
            }

            return listDTO;
        }
    }

}
