package com.fastcampus.board.user;

import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/joinForm")
    public String joinForm() {
        log.info("/joinForm GET");

        return "/joinForm";
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseEntity<?> join(@RequestBody @Valid UserRequest.JoinDTO joinDTO) {
        log.info("/join POST " + joinDTO);

        UserResponse.JoinDTO joinResponse = userService.save(joinDTO);
        return ResponseEntity.ok(joinResponse);
    }
}
