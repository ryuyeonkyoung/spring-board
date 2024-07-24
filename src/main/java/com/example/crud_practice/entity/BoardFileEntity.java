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
    // 파일 여러개 -> 게시글 하나
    @ManyToOne(fetch = FetchType.LAZY)
    // Hibernate: alter table board_file_table add constraint FKcfxqly70ddd02xbou0jxgh4o3 foreign key (board_id) references board_table (id)
    @JoinColumn(name = "board_id") // 외래키
    private BoardEntity boardEntity; // 부모 entity 타입으로 적어줘야함.

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity); // pk값이 아니라 부모 entity값을 넘겨줘야한다.
        return boardFileEntity;
    }
}
