package com.lgcns.beinstagramclone.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListItemDTO {
    private Integer postId;       // 게시글ID
    private String authorName;    // 회원이름
    private String content;       // 내용
    private LocalDateTime createdAt; // 작성시간

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>(); // 이미지들

    // JPQL 프로젝션과 정확히 매칭되는 4-인자 생성자
    public PostListItemDTO(Integer postId, String authorName, String content, LocalDateTime createdAt) {
        this.postId = postId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrls = new ArrayList<>();
    }
}
