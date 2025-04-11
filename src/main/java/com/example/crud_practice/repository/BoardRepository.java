package com.example.crud_practice.repository;

import com.example.crud_practice.entity.BoardEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 게시글 조회수 증가: 해당 게시글의 조회수(boardHits)를 1 증가시킴
    @Modifying // update, delete 쿼리를 실행할 때는 필수로 붙여야 함
    @Query("update BoardEntity b set b.boardHits = b.boardHits + 1 where b.id = :id") // JPQL을 사용
    void updateHits(@Param("id") Long id);

    /*
      SELECT b.id, b.title, ...
      FROM board b
      WHERE b.id < ?
      ORDER BY b.id DESC
      LIMIT ? OFFSET ?
     */
    // [1] JPQL 방식 (가장 일반적인 방식)
    @Query("SELECT b FROM BoardEntity b WHERE b.id < :cursor ORDER BY b.id DESC")
    List<BoardEntity> findByCursor(@Param("cursor") Long cursor, Pageable pageable); // Pageable 사용 가능

    /*
       SELECT *
       FROM board
       WHERE id < ?
       ORDER BY id DESC
       LIMIT ?
     */
    /*
    // [2] Native Query 방식 (복잡한 조건이거나 성능 최적화 필요 시 사용)
    @Query(value = "SELECT * FROM board b WHERE b.id < :cursor ORDER BY b.id DESC LIMIT :size", nativeQuery = true)
    List<BoardEntity> findByCursorNative(@Param("cursor") Long cursor, @Param("size") int size);
    */

    /*
      select board.id, board.title, ...
      from board
      where board.id < ?
      order by board.id desc
      limit ?
    */
    /*
    // [3] QueryDSL 방식 (동적 쿼리 작성 시 유용)
    // 사용 위치: 서비스 계층 (BoardService) → QueryFactory로 구현
    */
}