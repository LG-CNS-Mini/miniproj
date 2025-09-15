package com.lgcns.beinstagramclone.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListItemDTO {
    private String authorName;       // 회원이름
    private String content;          // 내용
    // private String hashtag;          // 해시태그
    private LocalDateTime createdAt; // 작성시간 (PostEntity의 createDate 매핑)
}