package com.fastcampus.board.user.controller;

import com.fastcampus.board.__core.security.PrincipalUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping({"/index", "/"})
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

    @GetMapping("/user/userForm")
    public String userForm(@AuthenticationPrincipal PrincipalUserDetail userDetail, Model model) {
        model.addAttribute("user", userDetail.getUser());
        return "/user/userForm";
    }

    @GetMapping("/user/updateForm")
    public String userUpdateForm(@AuthenticationPrincipal PrincipalUserDetail userDetail, Model model) {
        model.addAttribute("user", userDetail.getUser());
        return "/user/updateForm";
    }
}
