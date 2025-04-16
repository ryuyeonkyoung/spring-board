package com.example.crud_practice.mapper;

import com.example.crud_practice.dto.CommentDTO;
import com.example.crud_practice.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // 스프링 빈으로 등록해서 의존성 주입 가능
public interface CommentMapper {

    // CommentEntity + 외부 boardId → CommentDTO로 매핑
    // - MapStruct의 다중 파라미터 매핑(multisource mapping) 사용 예시
    // - boardId는 CommentEntity에 없는 값이므로 외부 파라미터로 따로 주입
    // - source와 target 이름이 같더라도 외부 파라미터는 명시적으로 선언하는 것이 명확함
    @Mapping(source = "boardId", target = "boardId") // 외부 파라미터 값을 DTO에 매핑

    // 아래 필드들은 CommentEntity 내부에서 가져옴
    @Mapping(source = "commentEntity.id", target = "id")
    @Mapping(source = "commentEntity.commentWriter", target = "commentWriter")
    @Mapping(source = "commentEntity.commentContents", target = "commentContents")
    @Mapping(source = "commentEntity.createdTime", target = "commentCreatedTime")
    CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId);
}