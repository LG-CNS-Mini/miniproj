package com.lgcns.beinstagramclone.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data @AllArgsConstructor
@Schema(description = "슬라이스 페이징 응답 DTO")
public class SliceResponseDTO<T> {

    @Schema(
        description = "조회된 데이터 목록",
        type = "array",
        example = "[{ \"postId\": 11, \"authorName\": \"홍길동\", \"content\": \"오늘은 날씨가 참 좋네요 \" }]"
    )
    private List<T> items;

    @Schema(description = "현재 페이지 번호 (0-based)", example = "0")
    private int page;

    @Schema(description = "페이지 크기", example = "5")
    private int size;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;
}