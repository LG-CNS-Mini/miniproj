package com.lgcns.beinstagramclone.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lgcns.beinstagramclone.comment.domain.dto.CommentResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시글 리스트 아이템 DTO (피드/게시글 목록 조회용)")
public class PostListItemDTO {

    @Schema(description = "게시글 ID", example = "11")
    private Integer postId;

    @Schema(description = "작성자 이름", example = "홍길동")
    private String authorName;

    @Schema(description = "게시글 내용", example = "오늘은 날씨가 참 좋네요")
    private String content;

    @Schema(description = "작성 시간", example = "2025-09-12T23:12:20")
    private LocalDateTime createdAt;

    @Builder.Default
    @Schema(description = "게시글 이미지 URL 목록 (상대 경로)", 
            type = "array",
            example = "[\"/images/2025/09/12/abc123.jpg\", \"/images/2025/09/12/xyz456.png\"]")
    private List<String> imageUrls = new ArrayList<>();

    @Builder.Default
    @Schema(description = "댓글 목록 (트리 구조)")
    private List<CommentResponseDTO> comments = new ArrayList<>();

    @Schema(description = "해시태그 목록", 
            type = "array",
            example = "[\"#여행\", \"#바다\"]")
    private List<String> hashtags;

    @Schema(description = "좋아요 총 개수", example = "42")
    private long likeCount;

    @Schema(description = "내가 좋아요 눌렀는지 여부", example = "true")
    private boolean likedByMe;

    // Projection용 생성자
    public PostListItemDTO(Integer postId, String authorName, String content,
                           LocalDateTime createdAt, List<CommentResponseDTO> comments) {
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
