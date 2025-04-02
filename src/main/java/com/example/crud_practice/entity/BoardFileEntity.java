package com.example.crud_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // 원본 파일 이름
    private String originalFileName;

    @Column // 저장된 파일 이름
    private String storedFileName;

    // 게시글에 파일이 여러 개 있을 수 있기 때문에 1:N 관계 설정
    @ManyToOne(fetch = FetchType.LAZY) // LAZY 로딩: 실제로 파일이 필요할 때만 로드
    @JoinColumn(name = "board_id") // 외래키 설정 (board_id)
    private BoardEntity boardEntity; // BoardEntity와의 연관 관계 설정

    // 파일 정보를 BoardEntity와 연결하여 BoardFileEntity 생성
    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity); // 파일은 게시글의 외래키로 연결
        return boardFileEntity;
    }
}
