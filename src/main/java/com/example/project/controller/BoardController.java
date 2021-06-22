package com.example.project.controller;

import com.example.project.Service.BoardService;
import com.example.project.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public String list(Model model, @PageableDefault(size = 10, sort = "id"
            , direction = Sort.Direction.ASC) Pageable pageable) {

        model.addAttribute("boardList", boardService.getBoardList(pageable));
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());
        model.addAttribute("check", boardService.getPageCheck(pageable));

        return "board/list";
    }

    @GetMapping("/post")
    public String write() {
        return "board/write";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @GetMapping("/post/{no}")
    public String detail(@PathVariable("no") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("boardDto", boardDto);

        return "board/detail";
    }

    @GetMapping("/board/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPost(keyword);
        model.addAttribute("boardList", boardDtoList);

        return "board/search";
    }

    @GetMapping("/post/edit/{no}")
    public String edit(@PathVariable("no") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("boardDto", boardDto);

        return "board/update";
    }

    @PutMapping("/post/edit/{no}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @DeleteMapping("/post/{no}")
    public String delete(@PathVariable("no") Long id)  {
        boardService.deletePost(id);
        return "redirect:/";
    }
}
