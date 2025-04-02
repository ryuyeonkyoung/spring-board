package com.example.crud_practice.entity;

import com.example.crud_practice.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {

    @Id // 기본 키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 (MySQL의 auto_increment)
    private Long id;

    @Column(length = 20, nullable = false) // 작성자
    private String boardWriter;

    @Column // 비밀번호
    private String boardPass;

    @Column // 게시글 제목
    private String boardTitle;

    @Column(length = 500) // 게시글 내용
    private String boardContents;

    @Column // 조회수
    private int boardHits;

    @Column // 파일 첨부 여부 (1: 첨부됨, 0: 첨부 안 됨)
    private int fileAttached;

    // 게시글에 여러 개의 첨부 파일이 있을 수 있기 때문에 1:N 관계 설정
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    // 게시글에 댓글이 여러 개 있을 수 있기 때문에 1:N 관계 설정
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    // BoardDTO를 BoardEntity로 변환하여 저장하기 위한 메서드
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0); // 초기 조회수는 0
        boardEntity.setFileAttached(0); // 파일 없음
        return boardEntity;
    }

    // 게시글 업데이트용 메서드 (id 포함)
    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId()); // 기존 id 필요
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;
    }

    // 파일이 있는 게시글 저장용 메서드
    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0); // 초기 조회수는 0
        boardEntity.setFileAttached(1); // 파일 있음
        return boardEntity;
    }
}
