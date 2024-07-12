package com.example.crud_practice.repository;

import com.example.crud_practice.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

}