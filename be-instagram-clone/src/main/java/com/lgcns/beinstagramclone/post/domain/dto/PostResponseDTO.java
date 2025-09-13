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
    private String hashtag;
    private LocalDateTime createDate;
    private String authorEmail;
    private List<String> imageUrls;

    public static PostResponseDTO fromEntity(PostEntity post) {
        List<String> urls = post.getImages() == null ? List.of()
                : post.getImages().stream()
                        .sorted(Comparator.comparing(PostImageEntity::getSortOrder))
                        .map(pi -> pi.getImage().getImageUrl())
                        .toList();

        return PostResponseDTO.builder()
                .postID(post.getPostID())
                .content(post.getContent())
                .hashtag(post.getHashtag())
                .createDate(post.getCreateDate())
                .authorEmail(post.getAuthor().getEmail())
                .imageUrls(urls)
                .build();
    }
}
