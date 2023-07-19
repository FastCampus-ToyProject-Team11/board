package com.fastcampus.board.comment;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.repository.BoardRepository;
import com.fastcampus.board.comment.dto.CommentRequest;
import com.fastcampus.board.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment saveComment(CommentRequest.saveDTO saveDTO, User user) {
        if (saveDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_SAVE_COMMENT);
        if (user == null) throw new Exception500(ErrorMessage.EMPTY_DATA_USER_FOR_SAVE_COMMENT);

        Optional<Board> boardOptional = boardRepository.findById(saveDTO.getBoardId());
        Board board = boardOptional.orElseThrow(() -> new Exception500(ErrorMessage.EMPTY_DATA_BOARD_FOR_SAVE_COMMENT));

        Comment comment = saveDTO.toEntityWithUserAndBoard(user, board);
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> findAllByBoardId(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }
}
