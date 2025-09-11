package com.lgcns.beinstagramclone.post.domain.dto;

import java.time.LocalDateTime;

import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostResponseDTO {

    private Integer postID;
    private String content;
    private String hashtag;
    private LocalDateTime createDate;
    private String authorEmail;

    public static PostResponseDTO fromEntity(PostEntity post) {
        return PostResponseDTO.builder()
                .postID(post.getPostID())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .createDate(post.getCreateDate())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }
}
