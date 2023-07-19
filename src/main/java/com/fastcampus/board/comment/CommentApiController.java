package com.fastcampus.board.comment;

import com.fastcampus.board.__core.security.PrincipalUserDetail;
import com.fastcampus.board.__core.util.ApiResponse;
import com.fastcampus.board.comment.dto.CommentRequest;
import com.fastcampus.board.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board/comment")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/")
    public ResponseEntity<?> save(@AuthenticationPrincipal PrincipalUserDetail userDetail,
                                  @RequestBody CommentRequest.saveDTO saveDTO,
                                  Errors errors) {
        log.info("/board/comment POST " + saveDTO);

        User user = userDetail.getUser();
        Comment comment = commentService.saveComment(saveDTO, user);

        return ResponseEntity.ok(ApiResponse.success(comment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long commentId) {
        log.info("/board/comment DELETE " + commentId);

        commentService.delete(commentId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
