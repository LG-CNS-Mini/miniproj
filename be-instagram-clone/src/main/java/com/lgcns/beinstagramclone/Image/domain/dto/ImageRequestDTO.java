package com.lgcns.beinstagramclone.Image.domain.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "AI 태그 생성을 위한 이미지 입력 DTO")
public class ImageRequestDTO {

    @ArraySchema(
        schema = @Schema(
            description = "AI 태깅할 이미지 파일",
            type = "string",
            format = "binary"
        )
    )
    @Schema(description = "이미지 파일 목록 (멀티파트 전송)")
    private List<MultipartFile> images = new ArrayList<>();
}