package com.lgcns.beinstagramclone.user.domain.dto;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "사용자 응답 DTO (회원 가입, 로그인)")
public class UserResponseDTO {

    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(description = "닉네임", example = "hwang123")
    private String nickname;

    @Schema(description = "프로필 이미지 URL (상대 경로)", example = "/images/2025/09/12/profile_123abc.png")
    private String userImageUrl;

    // 보안상 비밀번호는 응답에 포함하지 않는 게 권장됨
    // private String passwd;

    // 토큰은 필요하다면 로그인 API 응답 DTO에서 별도로 추가
    // private String accessToken;
    // private String refreshToken;

    public static UserResponseDTO fromEntity(UserEntity user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .userImageUrl(user.getUserImageUrl())
                .build();
    }
}