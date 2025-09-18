package com.lgcns.beinstagramclone.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 프로필 조회 응답 DTO")
public class UserProfileResponseDTO {

    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "프로필 이미지 URL (상대 경로)", 
            example = "/images/2025/09/12/profile_123abc.png")
    private String userImageUrl;

    @Schema(description = "사용자 닉네임", example = "hwang123")
    private String nickname;

    @Schema(description = "팔로워 수", example = "120")
    private Long followerCount;

    @Schema(description = "팔로잉 수", example = "80")
    private Long followingCount;

    @Schema(description = "게시글 수", example = "35")
    private Long postCount;

    @Schema(description = "팔로우 여부 (1 = 내가 팔로우함, 0 = 아님)", example = "1")
    private Long isFollow;
}