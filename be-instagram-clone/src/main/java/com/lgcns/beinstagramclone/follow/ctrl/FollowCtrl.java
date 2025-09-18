package com.lgcns.beinstagramclone.follow.ctrl;

import com.lgcns.beinstagramclone.follow.service.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follow")
@Tag(name = "Follow API", description = "팔로우/언팔로우 API")
public class FollowCtrl {

    @Autowired
    private FollowService followService;

    @PostMapping("/{followerEmail}/follow/{followingEmail}")
    @Operation(
        summary = "팔로우",
        description = "followerEmail 사용자가 followingEmail 사용자를 팔로우합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "팔로우 성공",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "사용자 없음",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "이미 팔로우 중",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<Void> follow(
            @Parameter(description = "팔로우를 시도하는 사용자 이메일", example = "me@example.com", required = true)
            @PathVariable String followerEmail,
            @Parameter(description = "팔로우 대상 사용자 이메일", example = "other@example.com", required = true)
            @PathVariable String followingEmail) {
        followService.follow(followerEmail, followingEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followerEmail}/unfollow/{followingEmail}")
    @Operation(
        summary = "언팔로우",
        description = "followerEmail 사용자가 followingEmail 사용자에 대한 팔로우를 해제합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "언팔로우 성공",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "팔로우 관계 없음/사용자 없음",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<Void> unfollow(
            @Parameter(description = "언팔로우를 시도하는 사용자 이메일", example = "me@example.com", required = true)
            @PathVariable String followerEmail,
            @Parameter(description = "언팔로우 대상 사용자 이메일", example = "other@example.com", required = true)
            @PathVariable String followingEmail) {
        followService.unfollow(followerEmail, followingEmail);
        return ResponseEntity.ok().build();
    }
}
