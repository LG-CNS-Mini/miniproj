package com.lgcns.beinstagramclone.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 검색 응답 DTO (검색 결과 목록용)")
public class UserSuggestDTO {

    @Schema(description = "사용자 이메일 (고유 ID)", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "닉네임", example = "hwang123")
    private String nickname;

    @Schema(description = "프로필 이미지 URL (상대 경로)", 
            example = "/images/2025/09/12/profile_123abc.png")
    private String userImageUrl;
}