package com.fastcampus.board.user;

import com.fastcampus.board.__core.security.JwtTokenProvider;
import com.fastcampus.board.__core.util.ApiResponse;
import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user/join")
    public ResponseEntity<ApiResponse.Result<UserResponse.JoinDTO>> join(
            @RequestBody @Valid UserRequest.JoinDTO joinDTO, Errors errors) {

        log.info("/user/join POST " + joinDTO);

        UserResponse.JoinDTO joinResponse = userService.save(joinDTO);
        return ResponseEntity.ok(ApiResponse.success(joinResponse));
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse.Result<UserResponse.LoginDTO>> login(
            @RequestBody @Valid UserRequest.LoginDTO loginDTO, Errors errors) {

        log.info("/user/login POST " + loginDTO);

        UserResponse.LoginDTOWithJWT loginDTOWithJWT = userService.login(loginDTO);

        String jwt = loginDTOWithJWT.getJwt();
        UserResponse.LoginDTO loginResponse = loginDTOWithJWT.getLoginDTO();

        return ResponseEntity.ok()
                .header(JwtTokenProvider.HEADER, jwt)
                .body(ApiResponse.success(loginResponse));
    }

    @PutMapping("/user/update")
    public ResponseEntity<ApiResponse.Result<Object>> update(
            @RequestBody @Valid UserRequest.UpdateDTO updateDTO, Errors errors) {

        log.info("/user/update PUT " + updateDTO);
        userService.update(updateDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/user/checkNickName")
    public ResponseEntity<ApiResponse.Result<Object>> checkNickName(
            @RequestBody @Valid UserRequest.CheckNickNameDTO checkNickNameDTO, Errors errors) {

        log.info("/user/checkNickName POST " + checkNickNameDTO);
        userService.checkNickName(checkNickNameDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/user/checkUsername")
    public ResponseEntity<ApiResponse.Result<Object>> checkUserName(
            @RequestBody @Valid UserRequest.CheckUsernameDTO checkUsernameDTO, Errors errors) {

        log.info("/user/checkUserName POST " + checkUsernameDTO);
        userService.checkUsername(checkUsernameDTO);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}


