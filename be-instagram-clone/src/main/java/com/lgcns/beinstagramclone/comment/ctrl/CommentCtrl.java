package com.lgcns.beinstagramclone.comment.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lgcns.beinstagramclone.comment.domain.dto.CommentRequestDTO;
import com.lgcns.beinstagramclone.comment.domain.dto.CommentResponseDTO;
import com.lgcns.beinstagramclone.comment.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Comment API", description = "댓글 관련 API")
public class CommentCtrl {

    @Autowired
    private CommentService commentService;

    @PostMapping("/register")
    @Operation(
        summary = "댓글 입력",
        description = "게시글에 새 댓글을 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "등록 성공",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CommentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검증 실패",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<CommentResponseDTO> insert(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "댓글 작성 요청 본문",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CommentRequestDTO.class)))
            @Valid @RequestBody CommentRequestDTO req) {
        return ResponseEntity.ok(commentService.insert(req));
    }

    @DeleteMapping("/{commentId}")
    @Operation(
        summary = "댓글 삭제",
        description = "댓글 작성자일 때 삭제할 수 있습니다.",
        responses = {
            @ApiResponse(responseCode = "204", description = "삭제 성공",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "권한 없음",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "인증 필요",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "댓글 ID", example = "123", required = true)
            @PathVariable("commentId") Integer commentId,
            @Parameter(description = "요청자(작성자) 이메일", example = "me@example.com", required = true)
            @RequestParam("authorEmail") String requesterEmail) {
        commentService.delete(commentId, requesterEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}")
    @Operation(
        summary = "게시글 단위 댓글 조회",
        description = "해당 게시글의 댓글을 트리 구조(대댓글 포함)로 반환합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CommentResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "게시글 없음",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<List<CommentResponseDTO>> getTree(
            @Parameter(description = "게시글 ID", example = "11", required = true)
            @PathVariable("postId") Integer postId
    ) {
        return ResponseEntity.ok(commentService.getTreeByPost(postId));
    }
}