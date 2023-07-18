package com.fastcampus.board.board.service;

import com.fastcampus.board.board.dto.BoardRequest;
import com.fastcampus.board.board.dto.BoardResponse;
import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.model.BoardFile;
import com.fastcampus.board.board.model.Role;
import com.fastcampus.board.board.repository.BoardFileRepository;
import com.fastcampus.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<BoardResponse.ListDTO> findAll(String keyword, Pageable pageable) {

        int page = pageable.getPageNumber() - 1;
        int pageLimit = 6;

        Pageable setPageable = PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boardEntities = null;

        if (keyword == null || keyword.isBlank()) {
            boardEntities = boardRepository.findAll(setPageable);
        } else {
            boardEntities = boardRepository.findAllByKeyword(keyword, setPageable);

        }
        Page<BoardResponse.ListDTO> boardDTOS = boardEntities.map(BoardResponse.ListDTO::toListDTO);

        return boardDTOS;
    }

    public Page<BoardResponse.ListDTO> findAllByCategory(Role role, String keyword, Pageable pageable) {

        int page = pageable.getPageNumber() - 1;
        int pageLimit = 6;
        Pageable setPageable = PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> boardEntities = null;


        if (keyword == null || keyword.isBlank()) {
            // 검색데이터가 없으면 해당 권한의 모든 게시글 목록 불러오기
            boardEntities = boardRepository.findAllByCategory(role, setPageable);
        } else {
            // 해당 권한의 게시글을 검색해서 불러오기
            boardEntities = boardRepository.findAllByKeywordCategory(role, keyword, setPageable);
        }

        Page<BoardResponse.ListDTO> boardDTOS = boardEntities.map(BoardResponse.ListDTO::toListDTO);

        return boardDTOS;
    }
}
