package com.lgcns.beinstagramclone.Image.ctrl;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageHashtagDTO;
import com.lgcns.beinstagramclone.Image.service.ImageAIService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/post/ai")
@Tag(name = "Image AI API", description = "이미지 분석/해시태그 추천 API")
public class ImageCtrl {
    @Autowired
    private ImageAIService imageAIService;

    @PostMapping(value = "/hashtag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "이미지 해시태그 추출",
        description = "여러 이미지 파일을 업로드하면 각 이미지에 대한 해시태그 목록을 반환합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "분석 성공",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ImageHashtagDTO.class))
                )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 / 파일 누락",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "415", description = "지원하지 않는 콘텐츠 타입",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<List<ImageHashtagDTO>> image(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "멀티파트 폼 데이터 (images: 이미지 파일 목록, 기타 필드 포함 가능)",
                content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = ImageRequestDTO.class)
                )
            )
            @ModelAttribute ImageRequestDTO request
    ) {
        List<ImageHashtagDTO> allResults = new ArrayList<>();
        for (MultipartFile image : request.getImages()) {
            List<String> hashtags = imageAIService.analyzeImage(image);

            ImageHashtagDTO dto = new ImageHashtagDTO();
            dto.setHashtags(hashtags);
            allResults.add(dto);
        }

        return ResponseEntity.ok().body(allResults);
    }
}
