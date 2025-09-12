package com.lgncs.inspire_restjpa.blog.domain.dto;

import java.util.List;

import com.lgncs.inspire_restjpa.blog.domain.entity.BlogEntity;
import com.lgncs.inspire_restjpa.comment.domain.dto.CommentResponseDTO;
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
public class BlogResponseDTO {
    private Integer blogId ; 
    private String  title ;
    private String  content ;
    private String  authorEmail ;

    // private List<String> comments ;
    private List<CommentResponseDTO> comments ; 

    public static BlogResponseDTO fromEntity(BlogEntity blog) {
        return BlogResponseDTO.builder()
                .blogId(blog.getBlogId())
                .title(blog.getTitle())
                .content(blog.getContent())
                .authorEmail(blog.getAuthor().getEmail())
                .comments(
                    blog.getComments().stream()
                        .map(e -> CommentResponseDTO.fromEntity(e))
                        .toList()
                )
                .build();  
    }

}
