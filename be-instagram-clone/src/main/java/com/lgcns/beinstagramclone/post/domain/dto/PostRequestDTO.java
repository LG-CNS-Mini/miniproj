package com.lgcns.beinstagramclone.post.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "게시글 작성 요청 DTO")
public class PostRequestDTO {

    @Schema(description = "게시글 내용", example = "오늘은 벚꽃 보러 다녀왔어요 ")
    private String content;

    @Schema(description = "해시태그 목록", type = "array", example = "[\"#봄날\", \"#산책\"]")
    private List<String> hashtags = new ArrayList<>();

    @Schema(description = "작성자 이메일", example = "user@example.com")
    private String authorEmail;

    @Schema(description = "업로드할 게시글 이미지 파일 목록 (멀티파트 전송)", type = "array", format = "binary")
    private List<MultipartFile> postImages = new ArrayList<>();

    public PostEntity toEntity(UserEntity author) {
        return PostEntity.builder()
                .content(this.content)
                .author(author)
                .build();
    }
}