package com.example.crud_practice.controller;

import com.example.crud_practice.dto.BoardDTO;
import com.example.crud_practice.dto.CommentDTO;
import com.example.crud_practice.service.BoardService;
import com.example.crud_practice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

// Controller은 HTTP요청을 처리하는 역할을 수행한다.
// Mapping으로 URL을 매핑하는 것이 HTTP 요청 처리 과정 중 하나이다.
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    // 페이지요청용
    @GetMapping("/save")
    public String saveForm() { return "save"; }

    // 데이터 전송용
    // boardDTO를 받아서 BoardService로 보내 저장 (거기선 DTO->entity)
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        if (boardDTO.getBoardFile().get(0).isEmpty())
            boardDTO.getBoardFile().clear();

        boardService.save(boardDTO);
        return "redirect:/board/paging";
    }

//    @GetMapping("/save")
//    public String saveForm() {
//        return "save"; // save.html을 반환하여 폼을 사용자에게 보여줍니다.
//    }
//    @PostMapping("/save")
//    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
//        boardService.save(boardDTO); // BoardDTO를 받아서 처리
//        return "index"; // 저장 후 index.html로 리디렉션
//    }


    @GetMapping("/")
    public String findAll(Model model) {
        /*
            controller에서는 service의 메소드를 이용한다.
            entity, dto, repository에 실제로 접근할 수 있는건 controller가 아니라 service이다.
         */
        List<BoardDTO> boardDTOList = boardService.findAll(); //dto 리스트 불러옴
        model.addAttribute("boardList", boardDTOList); //데이터를 뷰로 전달하기 위해 사용하는 메소드
        return "list"; //해당 뷰로 데이터가 전송된다.
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
        */
        boardService.updateHits(id);
        BoardDTO boardDTO= boardService.findById(id);
        /* 댓글 목록 가져오기 */
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
//        return "redirect:/board/" + boardDTO.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/paging";
    }

    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
//        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages(); //limit에 걸리면 보여지는 개수 조절

        // page 갯수 20개
        // 현재 사용자가 3페이지
        // 1 2 3
        // 현재 사용자가 7페이지
        // 7 8 9
        // 보여지는 페이지 갯수 3개
        // 총 페이지 갯수 8개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";

    }
}
