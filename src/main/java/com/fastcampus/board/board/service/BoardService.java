package com.fastcampus.board.board.service;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.board.dto.BoardRequest;
import com.fastcampus.board.board.dto.BoardResponse;
import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.repository.BoardRepository;
import com.fastcampus.board.user.Role;
import com.fastcampus.board.user.User;
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
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long save(BoardRequest.saveDTO dto, MultipartFile thumbnail, User user) throws IOException {

        String originalFilename = thumbnail.getOriginalFilename();
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        if (!thumbnail.isEmpty()) {
            String savePath = getSavePath(storedFileName);
            thumbnail.transferTo(new File(savePath));
        }

        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .thumbnailName(storedFileName)
                .user(user)
                .role(user.getRole())
                .build();

        return boardRepository.save(board).getId();
    }

    public String getSavePath(String storedFileName) {

        String homeDir = System.getProperty("user.home");
        String saveFolderName = "board-stored-img";

        File saveFolder = new File(homeDir + File.separator + saveFolderName);
        if (!saveFolder.exists()) {
            saveFolder.mkdir();
        }

        return saveFolder + File.separator + storedFileName;
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
            boardEntities = boardRepository.findAllByCategory(role, setPageable);
        } else {
            boardEntities = boardRepository.findAllByKeywordCategory(role, keyword, setPageable);
        }

        Page<BoardResponse.ListDTO> boardDTOS = boardEntities.map(BoardResponse.ListDTO::toListDTO);

        return boardDTOS;
    }

    @Transactional(readOnly = true)
    public BoardResponse.DetailDTO findById(Long id) {
        if (id == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_FIND_BOARD);

        Optional<Board> boardOptional = boardRepository.findById(id);
        Board board = boardOptional.orElseThrow(() -> new Exception500(ErrorMessage.NOT_FOUND_BOARD));

        return BoardResponse.DetailDTO.from(board);
    }

    @Transactional
    public void update(BoardRequest.UpdateDTO updateDTO) {

        Optional<Board> optionalBoard = boardRepository.findById(updateDTO.getId());

        if (optionalBoard.isPresent()) {

            Board boardPS = optionalBoard.get();
            boardPS.setTitle(updateDTO.getTitle());
            boardPS.setContent(updateDTO.getContent());

            boardRepository.save(boardPS);
        }
    }

    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


}
