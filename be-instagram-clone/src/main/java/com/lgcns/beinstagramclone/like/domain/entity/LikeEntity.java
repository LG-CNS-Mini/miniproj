package com.lgcns.beinstagramclone.like.domain.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@IdClass(LikeId.class) // 복합 키 클래스 지정
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {

    // 두 필드를 복합 키로 지정
    @Id 
    @Column(nullable = false)
    private String email;

    // 두 필드를 복합 키로 지정
    @Id
    @Column(nullable = false)
    private int postId;

    @Column(nullable = false)
    private Date createDate;
}