package com.fastcampus.board.board.controller;

import com.fastcampus.board.board.dto.BoardRequest;
import com.fastcampus.board.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardRequest.saveDTO saveDTO) throws IOException {

        String contentWithoutHTML = saveDTO.getContent().replaceAll("<[^>]*>", "");
        saveDTO.setContent(contentWithoutHTML);

        // 뷰로부터 넘어온 값과 dto의 필드와 일치하는 값을 setter를 호출해서 넣어줌
        Long id = boardService.save(saveDTO);
        return "redirect:/board/" + id;

    }
}
