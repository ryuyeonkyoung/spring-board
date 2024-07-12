package com.example.crud_practice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp //만들어진시간
    @Column(updatable = false) //수정시 관여안함
    private LocalDateTime createdTime;

    @UpdateTimestamp //수정시간
    @Column(insertable = false) //입력시 관여안함
    private LocalDateTime updatedTime;
}
