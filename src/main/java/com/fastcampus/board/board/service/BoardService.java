package com.fastcampus.board.board.service;

import com.fastcampus.board.board.dto.BoardRequest;
import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.model.BoardFile;
import com.fastcampus.board.board.repository.BoardFileRepository;
import com.fastcampus.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    @Transactional
    public Long save(BoardRequest.saveDTO dto) throws IOException {

        Long saveId;
        // 파일 첨부 여부에 따라 로직 분리
        if (dto.getThumbnail().isEmpty()) {
            // 첨부 파일 없음
            Board board = Board.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .fileAttached(0)
                    .build();

            saveId = boardRepository.save(board).getId();

        } else {
            // 첨부 파일 있음
            MultipartFile thumbnail = dto.getThumbnail();

            String originalFilename = thumbnail.getOriginalFilename();
            String storedFileName = UUID.randomUUID().toString() + "_" + originalFilename;
            String savePath = getSavePath(storedFileName);

            thumbnail.transferTo(new File(savePath));

            Board board = Board.builder()
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .fileAttached(1)
                    .build();

            saveId = boardRepository.save(board).getId();  // board_tb에 save (board_id)

            Board boardPS = boardRepository.findById(saveId).get();
            BoardFile boardFile = BoardFile.toBoardFile(boardPS, originalFilename, storedFileName);

            boardFileRepository.save(boardFile);    // board_file_tb에 save
        }
        return saveId;
    }

    public String getSavePath(String storedFileName) {

        String homeDir = System.getProperty("user.home");   // 사용자 홈 디렉토리
        String saveFolderName = "board-stored-img";

        File saveFolder = new File(homeDir + File.separator + saveFolderName);  // C/user/사용자/폴더명 (폴더까지의 경로)
        if (!saveFolder.exists()) {
            saveFolder.mkdir();
        }

        return saveFolder + File.separator + storedFileName; //  // C/user/사용자/폴더명/파일명 (파일까지의 경로)
    }
}
