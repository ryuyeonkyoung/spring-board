package com.example.crud_practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    // 자식 정의(부모 BoardEntity)
    @ManyToOne(fetch = FetchType.LAZY) // n 대 1. 게시글 하나에 파일 여러개.
    // Hibernate: alter table board_file_table add constraint FKcfxqly70ddd02xbou0jxgh4o3 foreign key (board_id) references board_table (id)
    @JoinColumn(name = "board_id") // 외래키
    private BoardEntity boardEntity; // 부모 entity 타입으로 적어줘야함.

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}
