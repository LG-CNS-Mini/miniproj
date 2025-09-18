package com.lgcns.beinstagramclone.like.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.like.domain.dto.LikeRequestDTO;
import com.lgcns.beinstagramclone.like.domain.dto.LikeResponseDTO;
import com.lgcns.beinstagramclone.like.service.LikeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v2/inspire/like")
@Tag(name = "Like API", description = "게시글 좋아요 관련 API")
public class LikeCtrl {
       @Autowired
    private LikeService likeService;

    // 좋아요 상태 및 개수 조회
    @Operation(
        summary = "좋아요 상태 및 개수 조회",
        description = "특정 사용자(email)의 특정 게시글(postId)에 대해 좋아요 여부와 총 좋아요 수를 반환합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{ \"isLiked\": true, \"likeCount\": 12 }"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
        @Parameter(description = "사용자 이메일", example = "me@example.com", required = true)
        @RequestParam String email,
        @Parameter(description = "게시글 ID", example = "11", required = true)
        @RequestParam int postId) {
        
        // 유효성 검사는 서비스 레이어나 별도의 로직으로 옮길 수 있습니다.
        
        LikeResponseDTO response = likeService.getLikeStatus(email, postId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("isLiked", response.isLiked());
        result.put("likeCount", response.getLikeCount());
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
    // 좋아요
    @Operation(
        summary = "좋아요 등록",
        description = "요청 본문에 포함된 이메일/게시글ID로 좋아요를 등록하고 현재 총 좋아요 수를 반환합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "좋아요 성공",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{ \"likeCount\": 13 }"))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패(필드 오류 등)",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    @PostMapping("/like")
    public ResponseEntity<Map<String, Integer>> userLikesPost(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "좋아요 등록 요청",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LikeRequestDTO.class))
        )
        @RequestBody @Valid LikeRequestDTO request,
        BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        LikeResponseDTO response = likeService.userlikesPost(request);
        
        Map<String, Integer> result = new HashMap<>();
        result.put("likeCount", response.getLikeCount());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 좋아요 취소
    @PostMapping("/unlike")
    @Operation(
        summary = "좋아요 취소",
        description = "요청 본문에 포함된 이메일/게시글ID로 좋아요를 취소하고 현재 총 좋아요 수를 반환합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{ \"likeCount\": 12 }"))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패(필드 오류 등)",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<Map<String, Integer>> userUnlikesPost(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "좋아요 취소 요청",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LikeRequestDTO.class))
        )
        @RequestBody @Valid LikeRequestDTO request,
        BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        LikeResponseDTO response = likeService.userUnlikesPost(request); 
        
        Map<String, Integer> result = new HashMap<>();
        result.put("likeCount", response.getLikeCount());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    
}
