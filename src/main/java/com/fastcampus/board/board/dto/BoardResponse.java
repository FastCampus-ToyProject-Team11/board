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

        private MultipartFile thumbnail;    
        private String originalFileName;    
        private String storedFileName;      
        private int fileAttached;         

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
