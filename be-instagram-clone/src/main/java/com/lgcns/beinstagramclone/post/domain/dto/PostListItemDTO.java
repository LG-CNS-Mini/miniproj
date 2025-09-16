package com.lgcns.beinstagramclone.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostListItemDTO {
    private Integer postId;      // 게시글ID
    private String authorName;       // 회원이름
    private String content;          // 내용
    // private String hashtag;          // 해시태그
    private LocalDateTime createdAt; // 작성시간 (PostEntity의 createDate 매핑)
    private List<String> imageUrls;       // 이미지들 

    public PostListItemDTO(String authorName, String content, LocalDateTime createdAt) {
        this.postId = postId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
    }
}
