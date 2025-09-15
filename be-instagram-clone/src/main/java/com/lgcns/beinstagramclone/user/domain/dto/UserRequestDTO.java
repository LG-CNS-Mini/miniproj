package com.lgcns.beinstagramclone.user.domain.dto;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

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
public class UserRequestDTO {

    @Email(message = "이메일 형식과 맞지 않습니다.")
    private String email    ;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]+$", message="패스워드 정책에 맞지 않습니다.")
    private String passwd   ;

    @NotNull(message = "이름을 입력해주세요.")
    private String name     ;

    @NotNull(message = "아이디(닉네임)를 입력해주세요.")
    private String nickname;  

    private String userImageUrl;

    // factory method pattern
    // dto -> entity
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
