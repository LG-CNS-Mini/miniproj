package com.lgcns.beinstagramclone.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;   // 게시물아이디 (PK)

    // 회원아이디 (FK → user_entity.email)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email", referencedColumnName = "email")
    private UserEntity author;

    @Column(columnDefinition = "text")
    private String content; // 내용

    @Column(length = 255)
    private String hashtag; // 해시태그

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now(); // 작성시간
}
