package com.example.crud_practice.controller;

import com.example.crud_practice.dto.CommentDTO;
import com.example.crud_practice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    // 댓글 저장 및 댓글 목록 반환
    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        System.out.println("commentDTO = " + commentDTO);

        // 1. 댓글 저장 처리
        Long saveResult = commentService.save(commentDTO);
        if (saveResult != null) {
            // 2. 댓글 저장이 성공하면 해당 게시글의 모든 댓글 목록을 반환
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK); // 댓글 목록 반환 (200 OK)
        } else {
            // 3. 댓글 저장 실패시, 게시글이 존재하지 않음을 알려줌
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND); // 게시글 없음 (404 Not Found)
        }
    }
}
