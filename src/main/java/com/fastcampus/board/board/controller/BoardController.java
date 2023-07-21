package com.fastcampus.board.board.controller;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception400;
import com.fastcampus.board.__core.security.PrincipalUserDetail;
import com.fastcampus.board.board.dto.BoardRequest;
import com.fastcampus.board.board.dto.BoardResponse;
import com.fastcampus.board.board.service.BoardService;
import com.fastcampus.board.comment.Comment;
import com.fastcampus.board.comment.CommentService;
import com.fastcampus.board.user.Role;
import com.fastcampus.board.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm(){
        return "board/save";
    }

    @PostMapping("/save")
    public String save(@AuthenticationPrincipal PrincipalUserDetail userDetail,
                       @ModelAttribute @Valid BoardRequest.saveDTO saveDTO, Errors errors,
                       @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {

        validateInputWithoutHTML(saveDTO.getContent(), ErrorMessage.EMPTY_DATA_FOR_SAVE_CONTENT);
        validateInputWithoutHTML(saveDTO.getTitle(), ErrorMessage.EMPTY_DATA_FOR_SAVE_TITLE);

        User user = userDetail.getUser();
        Long id = boardService.save(saveDTO, thumbnail, user);

        return "redirect:/board/" + id;
    }

    @GetMapping("/list")
    public String boardList(@PageableDefault(page=1) Pageable pageable, Model model,
                            @RequestParam(value = "keyword", required = false) String keyword) {

        Page<BoardResponse.ListDTO> boardList = boardService.findAll(keyword, pageable);

        for (BoardResponse.ListDTO dto : boardList) {
            String contentWithoutHTML = dto.getContent().replaceAll("<[^>]*>", "");
            dto.setContent(contentWithoutHTML);
        }

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/list";
    }

    @GetMapping("/list/{category}")
    public String boardListByRole(@PageableDefault(page=1) Pageable pageable, Model model, @PathVariable String category,
                                  @RequestParam(value = "keyword", required = false) String keyword) {

        Role role;
        if (category.equals(Role.SESAC.name())) {
            role = Role.SESAC;
        } else {
            role = Role.EXCELLENT;
        }

        Page<BoardResponse.ListDTO> boardList = boardService.findAllByRole(role, keyword, pageable);

        for (BoardResponse.ListDTO dto : boardList) {
            String contentWithoutHTML = dto.getContent().replaceAll("<[^>]*>", "");
            dto.setContent(contentWithoutHTML);
        }

        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("category", category);
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

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model, @PageableDefault(page=1) Pageable pageable) {

        BoardResponse.DetailDTO board = boardService.findById(id);
        List<Comment> comments = commentService.findAllByBoardId(id);

        model.addAttribute("board", board);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("comments", comments);

        return "board/detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {

        BoardResponse.DetailDTO boardDetailDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDetailDTO);

        return "board/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute @Valid BoardRequest.UpdateDTO updateDTO, Errors errors) {

        validateInputWithoutHTML(updateDTO.getContent(), ErrorMessage.EMPTY_DATA_FOR_SAVE_CONTENT);
        validateInputWithoutHTML(updateDTO.getTitle(), ErrorMessage.EMPTY_DATA_FOR_SAVE_TITLE);

        boardService.update(updateDTO);

        return "redirect:/board/" + updateDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        boardService.delete(id);

        return "redirect:/board/list";
    }

    private void validateInputWithoutHTML(String input, String errorMessage) {
        String inputWithoutHTML = input.replaceAll("<[^>]*>", "").replace("&nbsp;", " ");
        System.out.println("Validating input: " + inputWithoutHTML);

        if (inputWithoutHTML.isBlank()) {
            throw new Exception400(input, errorMessage);
        }
    }

}
