package com.example.crud_practice.mapper;

import com.example.crud_practice.dto.BoardPageResponseDTO;
import com.example.crud_practice.dto.BoardRequestDTO;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.BoardFileEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    // boardEntity → BoardRequestDTO
    @Mapping(target = "originalFileName", source = "boardFileEntityList", qualifiedByName = "toOriginalFileNameList")
    @Mapping(target = "storedFileName", source = "boardFileEntityList", qualifiedByName = "toStoredFileNameList")
    @Mapping(target = "fileAttached", expression = "java(boardEntity.getFileAttached())")
    BoardRequestDTO toBoardRequestDTO(BoardEntity boardEntity);

    // boardEntity → BoardPageResponseDTO (offset paging)
    @Named("toBoardPageResponseDTO")
    BoardPageResponseDTO toOffsetPageDTO(BoardEntity boardEntity);

    // boardEntity → BoardPageResponseDTO (cursor paging)
    @Named("toCursorPageDTO")
    default BoardPageResponseDTO toCursorPageDTO(BoardEntity boardEntity) {
        return toOffsetPageDTO(boardEntity);
    }

    // 파일명 리스트 매핑 메서드
    // (List<BoardFileEntity> → List<String> 자동 매핑 불가 → @Named 커스텀 매핑 메서드 생성)
    @Named("toOriginalFileNameList")
    default List<String> toOriginalFileNameList(List<BoardFileEntity> fileEntityList) {
        if (fileEntityList == null) return null;
        return fileEntityList.stream()
                .map(BoardFileEntity::getOriginalFileName) // List<BoardFileEntity> → List<String>
                .collect(Collectors.toList());
    }

    @Named("toStoredFileNameList")
    default List<String> toStoredFileNameList(List<BoardFileEntity> fileEntityList) {
        if (fileEntityList == null) return null;
        return fileEntityList.stream()
                .map(BoardFileEntity::getStoredFileName)
                .collect(Collectors.toList());
    }
}