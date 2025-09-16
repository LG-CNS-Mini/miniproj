package com.lgcns.beinstagramclone.like.domain.dto;

import java.util.Date;

import com.lgcns.beinstagramclone.like.domain.entity.LikeEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeRequestDTO {

    @Email(message = "이메일 형식과 맞지 않습니다.")
    @NotNull(message = "좋아요를 누른 회원 아이디를 입력하세요.")
    private String  email       ;

    @NotNull(message = "좋아요가 눌러진 포스트 아이디를 입력하세요.")
    private int  postId      ;

    public LikeEntity toEntity() {
        return LikeEntity.builder()
                    .email(email)
                    .postId(postId)
                    .build();
    }
}
