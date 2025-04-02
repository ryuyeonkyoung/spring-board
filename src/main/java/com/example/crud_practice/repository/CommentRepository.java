package com.example.crud_practice.repository;

import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    // 게시글에 해당하는 모든 댓글을 id 기준 내림차순으로 조회
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);

}
