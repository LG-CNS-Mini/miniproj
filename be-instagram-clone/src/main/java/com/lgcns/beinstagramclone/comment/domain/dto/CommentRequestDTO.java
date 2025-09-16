package com.lgcns.beinstagramclone.comment.domain.dto;

import com.lgcns.beinstagramclone.comment.domain.entity.CommentEntity;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

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
public class CommentRequestDTO {

    @NotNull
    private Integer postId;

    private Integer parentId;
    @NotBlank
    @Size(max = 500)
    private String content;
    
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