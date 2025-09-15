package com.lgcns.beinstagramclone.user.domain.dto;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

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
public class UserResponseDTO {

    private String email, passwd, name, userImageUrl,nickname;

    // token
    // private String accessToken;
    // private String refreshToken;

    // static factory method pattern
    // entity -> dto
    public static UserResponseDTO fromEntity(UserEntity user) {
        return UserResponseDTO.builder()
                    .email(user.getEmail())
                    .passwd(user.getPasswd())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .userImageUrl(user.getUserImageUrl())
                    .build();
    }
}
