package com.fastcampus.board.user;

import com.fastcampus.board.__core.security.PrincipalUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/user")
    public String user(HttpServletRequest request, Authentication authentication) {
        log.info("/user GET jwt: " + request.getHeader("Authorization"));
        log.info("auth: " + authentication.getPrincipal());
        return "/user";
    }

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal PrincipalUserDetail userDetail) {
        log.info("/index GET userDetail:" + userDetail);
        return "/index";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "/user/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "/user/joinForm";
    }
}
