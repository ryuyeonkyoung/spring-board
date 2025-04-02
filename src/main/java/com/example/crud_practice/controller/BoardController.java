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

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    // 게시글 작성 폼 출력
    @GetMapping("/save")
    public String saveForm() { return "save"; }

    // 게시글 저장 처리
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);

        // 파일이 없는 경우 빈 리스트로 처리
        if (boardDTO.getBoardFile().get(0).isEmpty())
            boardDTO.getBoardFile().clear();

        boardService.save(boardDTO);
        return "redirect:/board/paging";
    }

    // 전체 게시글 목록 조회
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

    // 게시글 상세 조회 (조회수 증가 + 댓글 포함)
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {

        boardService.updateHits(id); // 조회주 증가
        BoardDTO boardDTO= boardService.findById(id); // 게시글 데이터 조회
        List<CommentDTO> commentDTOList = commentService.findAll(id); // 댓글 목록 조회

        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());

        return "detail";
    }

    // 게시글 수정 폼 출력
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";
    }

    // 게시글 수정 처리
    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
//        return "redirect:/board/" + boardDTO.getId();
    }

    // 게시글 삭제 처리
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/paging";
    }

    // 게시글 페이징 목록 조회
    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<BoardDTO> boardList = boardService.paging(pageable);

        int blockLimit = 3; // 한번에 보여줄 페이지 수
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages(); //limit에 걸리면 보여지는 개수 조절

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "paging";
    }
}
