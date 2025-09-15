package com.lgcns.beinstagramclone.user.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSuggestDTO {
    private String email;
    private String name;
    private String nickname;
    private String userImageUrl;
}