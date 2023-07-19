package com.fastcampus.board.comment;

import com.fastcampus.board.__core.security.PrincipalUserDetail;
import com.fastcampus.board.__core.util.ApiResponse;
import com.fastcampus.board.comment.dto.CommentRequest;
import com.fastcampus.board.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board/comment")
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/")
    public ResponseEntity<?> save(@AuthenticationPrincipal PrincipalUserDetail userDetail,
                                  @RequestBody CommentRequest.saveDTO saveDTO) {
        log.info("/board/save POST " + saveDTO);

        User user = userDetail.getUser();
        Comment comment = commentService.saveComment(saveDTO, user);

        return ResponseEntity.ok(ApiResponse.success(comment));
    }
}
