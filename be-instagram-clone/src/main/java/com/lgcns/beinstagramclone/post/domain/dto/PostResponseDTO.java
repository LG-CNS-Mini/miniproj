package com.lgcns.beinstagramclone.post.domain.dto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.domain.entity.PostImageEntity;

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

    // ✅ 단일 문자열 → 리스트로 변경
    private List<String> hashtags;

    private LocalDateTime createDate;
    private String authorEmail;
    private List<String> imageUrls;

    public static PostResponseDTO fromEntity(PostEntity post) {
        List<String> urls = (post.getImages() == null) ? List.of()
                : post.getImages().stream()
                        .sorted(Comparator.comparing(PostImageEntity::getSortOrder))
                        .map(pi -> pi.getImage().getImageUrl())
                        .toList();
        List<String> tags = (post.getTags() == null) ? List.of()
                : post.getTags().stream()
                        .map(tag -> tag.getHashtag().getName())
                        .distinct() 
                        .toList();

        return PostResponseDTO.builder()
                .postID(post.getPostID())
                .content(post.getContent())
                .hashtags(tags) 
                .createDate(post.getCreateDate())
                .authorEmail(post.getAuthor().getEmail())
                .imageUrls(urls)
                .build();
    }
}
