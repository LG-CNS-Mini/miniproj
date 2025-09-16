package com.lgcns.beinstagramclone.comment.domain.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lgcns.beinstagramclone.comment.domain.entity.CommentEntity;

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
public class CommentResponseDTO {

    private Integer commentId;
    private Integer postId;
    private Integer parentId;
    private Integer depth;
    private String content;

    private String authorEmail;
    private String authorNickname;
    private String authorProfileImageUrl;
    private LocalDateTime createdAt;

    @Builder.Default
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
