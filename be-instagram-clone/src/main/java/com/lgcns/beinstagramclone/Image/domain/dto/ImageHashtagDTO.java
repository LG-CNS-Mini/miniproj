package com.lgcns.beinstagramclone.Image.domain.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "AI 이미지 분석 후 생성된 해시태그 응답 DTO")
public class ImageHashtagDTO {

    @Schema(description = "AI가 생성한 해시태그 목록", type = "array", example = "[\"#여행\", \"#바다\", \"#휴가\"]")
    private List<String> hashtags;
}