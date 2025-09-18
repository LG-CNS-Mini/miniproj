package com.lgcns.beinstagramclone.user.domain.dto;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Schema(description = "사용자 요청 DTO (회원 가입, 로그인)")
public class UserRequestDTO {

    @Email(message = "이메일 형식과 맞지 않습니다.")
    @Schema(description = "사용자 이메일 (로그인 ID)", example = "user@example.com")
    private String email;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$", message = "패스워드 정책에 맞지 않습니다.")
    @Schema(description = "비밀번호 (영문 대소문자, 숫자 조합)", example = "Passw0rd123")
    private String passwd;

    @NotNull(message = "이름을 입력해주세요.")
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;

    @NotNull(message = "아이디(닉네임)를 입력해주세요.")
    @Schema(description = "닉네임 (중복 불가)", example = "hwang123")
    private String nickname;

    @Schema(description = "사용자 프로필 이미지 URL (상대 경로)", example = "/images/2025/09/12/profile_abc123.png")
    private String userImageUrl;

    // factory method pattern
    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(email)
                .passwd(passwd)
                .name(name)
                .nickname(nickname)
                .userImageUrl(userImageUrl)
                .build();
    }
}