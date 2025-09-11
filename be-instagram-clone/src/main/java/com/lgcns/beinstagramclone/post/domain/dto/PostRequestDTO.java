package com.lgcns.beinstagramclone.post.domain.dto;

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
public class PostRequestDTO {
    private String content; 
    private String hashtag; 

    public PostEntity toEntity() {
        return PostEntity.builder()
                .content(this.content)
                .hashtag(this.hashtag)
                .build();
    }
}
