package com.lgncs.inspire_restjpa.comment.domain.dto;


import com.lgncs.inspire_restjpa.comment.domain.entity.CommentEntity;

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
public class CommentResponseDTO {
    private Integer commentId ; 
    private String  comment ; 
    private Integer blogId ;

    public static CommentResponseDTO fromEntity(CommentEntity entity) {
        return CommentResponseDTO.builder()
                .commentId(entity.getCommentId())
                .comment(entity.getComment())
                .blogId(entity.getBlog().getBlogId())
                .build();
    }
}







