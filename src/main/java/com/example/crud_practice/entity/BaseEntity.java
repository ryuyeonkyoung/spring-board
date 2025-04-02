package com.example.crud_practice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 다른 엔티티들에서 공통적으로 사용하는 생성 시간과 수정 시간을 관리하기 위한 기본 엔티티
@MappedSuperclass // 이 클래스를 상속한 엔티티들은 createdTime, updatedTime 필드를 상속받음
@EntityListeners(AuditingEntityListener.class) // JPA Auditing을 사용하여 자동으로 createdTime, updatedTime을 관리
@Getter
public class BaseEntity {

    @CreationTimestamp // 엔티티 생성 시 자동으로 생성 시간 설정
    @Column(updatable = false) // createdTime은 엔티티가 수정될 때 변경되지 않음
    private LocalDateTime createdTime;

    @UpdateTimestamp // 엔티티 수정 시 자동으로 수정 시간 업데이트
    @Column(insertable = false) // updatedTime은 엔티티가 처음 저장될 때에는 값이 없으므로 삽입 시 관여하지 않음
    private LocalDateTime updatedTime;
}
