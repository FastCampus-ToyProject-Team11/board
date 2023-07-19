package com.fastcampus.board.comment;

import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.board.model.Board;
import com.fastcampus.board.board.repository.BoardRepository;
import com.fastcampus.board.comment.dto.CommentRequest;
import com.fastcampus.board.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @DisplayName("댓글 등록 성공 테스트")
    @Test
    void save_Success_Test() {
        // Given
        Mockito.when(boardRepository.findById(ArgumentMatchers.anyLong()))
                .then(invocation -> {
                    Long boardId = invocation.getArgument(0);
                    Board board = Board.builder().id(boardId).build();
                    return Optional.of(board);
                });

        Mockito.when(commentRepository.save(ArgumentMatchers.any(Comment.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        User user = User.builder()
                .username("testUser")
                .build();

        CommentRequest.saveDTO saveDTO =
                CommentRequest.saveDTO.builder()
                        .boardId(1L)
                        .content("test")
                        .build();

        // When
        Comment actual = commentService.saveComment(saveDTO, user);

        // Then
        Assertions.assertEquals(1L, actual.getBoard().getId());
        Assertions.assertEquals("test", actual.getContent());
    }

    @DisplayName("댓글 등록 실패 테스트 - NULL DTO")
    @Test
    void save_Failed_Test_NullDTO() {
        // Given
        User user = User.builder()
                .username("testUser")
                .build();

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> commentService.saveComment(null, user));
    }

    @DisplayName("댓글 등록 실패 테스트 - NULL USER")
    @Test
    void save_Failed_Test_NullUser() {
        // Given
        CommentRequest.saveDTO saveDTO =
                CommentRequest.saveDTO.builder()
                        .boardId(1L)
                        .content("test")
                        .build();

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> commentService.saveComment(saveDTO, null));
    }
}