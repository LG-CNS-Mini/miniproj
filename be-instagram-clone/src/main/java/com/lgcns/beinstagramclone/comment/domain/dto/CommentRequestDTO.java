package com.lgcns.beinstagramclone.comment.domain.dto;

import com.lgcns.beinstagramclone.comment.domain.entity.CommentEntity;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(description = "댓글 작성 요청 DTO")
public class CommentRequestDTO {

    @NotNull
    @Schema(description = "댓글이 달릴 게시글 ID", example = "11")
    private Integer postId;

    @Schema(description = "부모 댓글 ID (대댓글일 경우만 필요)", example = "100")
    private Integer parentId;

    @NotBlank
    @Size(max = 500)
    @Schema(description = "댓글 내용 (최대 500자)", example = "정말 좋은 글이네요!")
    private String content;

    @Schema(description = "작성자 이메일", example = "user@example.com")
    private String authorEmail;

    public CommentEntity toEntity(UserEntity author, PostEntity post, CommentEntity parent, int depth) {
        return CommentEntity.builder()
                .content(this.content)
                .author(author)
                .post(post)
                .parent(parent)
                .depth(depth)
                .build();
    }
}