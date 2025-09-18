package com.lgcns.beinstagramclone.like.domain.dto;

import java.util.Date;

import com.lgcns.beinstagramclone.like.domain.entity.LikeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "좋아요 요청 DTO")
public class LikeRequestDTO {

    @Email(message = "이메일 형식과 맞지 않습니다.")
    @NotNull(message = "좋아요를 누른 회원 이메일을 입력하세요.")
    @Schema(description = "좋아요 누른 회원 이메일", example = "user@example.com")
    private String email;

    @NotNull(message = "좋아요가 눌러진 게시글 ID를 입력하세요.")
    @Schema(description = "좋아요 대상 게시글 ID", example = "11")
    private int postId;

    public LikeEntity toEntity() {
        return LikeEntity.builder()
                .email(email)
                .postId(postId)
                .build();
    }
}