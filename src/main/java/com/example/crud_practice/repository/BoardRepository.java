package com.example.crud_practice.repository;

import com.example.crud_practice.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 게시글 조회수 증가: 해당 게시글의 조회수(boardHits)를 1 증가시킴
    @Modifying // update, delete 쿼리를 실행할 때는 필수로 붙여야 함
    @Query("update BoardEntity b set b.boardHits = b.boardHits + 1 where b.id = :id") // JPQL을 사용
    void updateHits(@Param("id") Long id);
}