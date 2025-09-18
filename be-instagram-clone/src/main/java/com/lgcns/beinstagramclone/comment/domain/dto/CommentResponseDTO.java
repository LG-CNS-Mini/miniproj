package com.lgcns.beinstagramclone.comment.domain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lgcns.beinstagramclone.comment.domain.entity.CommentEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "댓글 응답 DTO")
public class CommentResponseDTO {

    @Schema(description = "댓글 ID", example = "101")
    private Integer commentId;

    @Schema(description = "게시글 ID", example = "11")
    private Integer postId;

    @Schema(description = "부모 댓글 ID (대댓글일 경우만 존재)", example = "100")
    private Integer parentId;

    @Schema(description = "댓글 깊이 (0=댓글, 1=대댓글)", example = "0")
    private Integer depth;

    @Schema(description = "댓글 내용", example = "좋은 글 잘 보고 갑니다!")
    private String content;

    @Schema(description = "작성자 이메일", example = "user@example.com")
    private String authorEmail;

    @Schema(description = "작성자 닉네임", example = "홍길동")
    private String authorNickname;

    @Schema(description = "작성자 프로필 이미지 URL (상대 경로)", example = "/images/2025/09/12/abc123.jpg")
    private String authorProfileImageUrl;

    @Schema(description = "댓글 작성일시", example = "2025-09-17T14:25:30")
    private LocalDateTime createdAt;

    @Builder.Default
    @Schema(description = "대댓글 리스트")
    private List<CommentResponseDTO> children = new ArrayList<>();

    public static CommentResponseDTO fromEntity(CommentEntity e) {
        return CommentResponseDTO.builder()
                .commentId(e.getCommentId())
                .postId(e.getPost().getPostID())
                .parentId(e.getParent() != null ? e.getParent().getCommentId() : null)
                .depth(e.getDepth())
                .content(e.getContent())
                .authorEmail(e.getAuthor().getEmail())
                .authorNickname(e.getAuthor().getNickname())
                .authorProfileImageUrl(e.getAuthor().getUserImageUrl())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
