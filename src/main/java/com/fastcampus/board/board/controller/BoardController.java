package com.fastcampus.board.board.controller;

import com.fastcampus.board.board.dto.BoardRequest;
import com.fastcampus.board.board.dto.BoardResponse;
import com.fastcampus.board.board.model.Role;
import com.fastcampus.board.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;


@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/save")
    public String saveForm(){

        return "board/save";
      
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardRequest.saveDTO saveDTO) throws IOException {

        String contentWithoutHTML = saveDTO.getContent().replaceAll("<[^>]*>", "");
        saveDTO.setContent(contentWithoutHTML);

        Long id = boardService.save(saveDTO);
        return "redirect:/board/" + id;

    }

    @GetMapping("/list")
    public String boardList(@PageableDefault(page=1) Pageable pageable, Model model,
                            @RequestParam(value = "keyword", required = false) String keyword) {

        Page<BoardResponse.ListDTO> boardList = boardService.findAll(keyword, pageable);

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; 
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/list";
    }

    @GetMapping("/list/{category}")
    public String boardListByRole(@PageableDefault(page=1) Pageable pageable, Model model,
                                  @RequestParam(value = "keyword", required = false) String keyword, @RequestParam Role role) {

        Page<BoardResponse.ListDTO> boardList = boardService.findAllByCategory(role, keyword, pageable);

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; 
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/list";
    }


    @ResponseBody
    @GetMapping("/image/{storedFileName}")
    public Resource image(@PathVariable String storedFileName) throws MalformedURLException {

        String savePath = boardService.getSavePath(storedFileName);
        return new UrlResource("file:" + savePath);
    }

}
