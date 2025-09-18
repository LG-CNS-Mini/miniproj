package com.lgcns.beinstagramclone.user.domain.dto;

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
public class UserProfileResponseDTO {
    private String email;
    private String name;
    private String userImageUrl;
    private String nickname;
    private Long followerCount;
    private Long followingCount;
    private Long postCount;
    private Long isFollow;
}