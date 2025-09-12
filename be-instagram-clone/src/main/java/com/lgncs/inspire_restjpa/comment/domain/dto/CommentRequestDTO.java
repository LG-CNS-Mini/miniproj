package com.lgncs.inspire_restjpa.comment.domain.dto;


import com.lgncs.inspire_restjpa.blog.domain.entity.BlogEntity;
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
public class CommentRequestDTO {
    private String  comment ;
    private Integer blogId ;

    public CommentEntity toEntity(BlogEntity blog) {
        return CommentEntity.builder()
                .comment(comment)
                .blog(blog)
                .build() ; 
    }
}

