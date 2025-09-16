package com.lgcns.beinstagramclone.comment.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.comment.domain.dto.CommentRequestDTO;
import com.lgcns.beinstagramclone.comment.domain.dto.CommentResponseDTO;
import com.lgcns.beinstagramclone.comment.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
public class CommentCtrl {

    @Autowired
    private CommentService commentService;

    @PostMapping("/register")
    @Operation(summary = "댓글 입력")
    public ResponseEntity<CommentResponseDTO> insert(
            @Valid @RequestBody CommentRequestDTO req) {
        return ResponseEntity.ok(commentService.insert(req));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제")
    public ResponseEntity<Void> delete(
            @PathVariable("commentId") Integer commentId,
            @RequestHeader("authorEmail") String requesterEmail) {
        commentService.delete(commentId, requesterEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 단위 댓글 조회")
    public ResponseEntity<List<CommentResponseDTO>> getTree(
            @PathVariable("postId") Integer postId
    ) {
        return ResponseEntity.ok(commentService.getTreeByPost(postId));
    }
}