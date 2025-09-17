package com.lgcns.beinstagramclone.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lgcns.beinstagramclone.comment.domain.dto.CommentResponseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListItemDTO {
    private Integer postId; // 게시글ID
    private String authorName; // 회원이름
    private String content; // 내용
    private LocalDateTime createdAt; // 작성시간

    @Builder.Default
    private List<String> imageUrls = new ArrayList<>(); // 이미지들

    @Builder.Default
    private List<CommentResponseDTO> comments = new ArrayList<>();

    private List<String> hashtags;
    
    private long likeCount; // 총 종아요 수
    private boolean likedByMe; // 좋아요 여부

    // Projection용 생성자
    public PostListItemDTO(Integer postId, String authorName, String content, LocalDateTime createdAt,List<CommentResponseDTO> comments) {
        this.postId = postId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrls = new ArrayList<>();
        this.likeCount = 0L;
        this.likedByMe = false;
        this.comments = comments != null ? comments : new ArrayList<>();
    }

    public PostListItemDTO(String authorName, String content, LocalDateTime createDate) {
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createDate;
    }

}
