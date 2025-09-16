package com.lgcns.beinstagramclone.like.domain.dto;

import java.util.Date;

import com.lgcns.beinstagramclone.like.domain.entity.LikeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeResponseDTO {
    
    private String email;

    private int postId;

    private Date createDate;

    // 사용자의 좋아요 상태를 나타내는 필드
    private boolean isLiked;

    // 해당 게시물의 총 좋아요 개수를 나타내는 필드
    private int likeCount;

    public static LikeResponseDTO fromEntity(LikeEntity like) {
        return LikeResponseDTO.builder()
                .email(like.getEmail())
                .postId(like.getPostId())
                .createDate(like.getCreateDate())
                .build();
    }
    
}
