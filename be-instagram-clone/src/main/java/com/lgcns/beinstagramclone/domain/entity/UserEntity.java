package com.lgcns.beinstagramclone.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @Column(length = 255)
    private String email;   // 회원아이디 (PK)

    @Column(nullable = false, length = 255)
    private String passwd;  // 회원패스워드

    @Column(nullable = false, length = 255)
    private String name;    // 회원이름
}