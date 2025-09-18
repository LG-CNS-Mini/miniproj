package com.lgcns.beinstagramclone.post.domain.dto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import com.lgcns.beinstagramclone.comment.domain.dto.CommentResponseDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.domain.entity.PostImageEntity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "게시글 응답 DTO")
public class PostResponseDTO {

        @Schema(description = "게시글 ID", example = "11")
        private Integer postID;

        @Schema(description = "게시글 내용", example = "오늘 날씨가 너무 좋아요")
        private String content;

        @Schema(description = "해시태그 목록", type = "array", example = "[\"#봄날\", \"#산책\"]")
        private List<String> hashtags;

        @Schema(description = "게시글 작성일시", example = "2025-09-12T23:12:20")
        private LocalDateTime createDate;

        @Schema(description = "작성자 이메일", example = "user@example.com")
        private String authorEmail;

        @Schema(description = "게시글 이미지 URL 목록 (상대 경로)", example = "[\"/images/2025/09/12/abc123.jpg\", \"/images/2025/09/12/xyz456.png\"]")
        private List<String> imageUrls;

        @Schema(description = "댓글 목록 (트리 구조)")
        private List<CommentResponseDTO> comments;

        @Schema(description = "좋아요 개수", example = "42")
        private long likeCount;

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
