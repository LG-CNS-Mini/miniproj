package com.lgcns.beinstagramclone.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "프로필 이미지 응답 DTO")
public class ProfileImageResponseDTO {

    @Schema(description = "사용자 프로필 이미지 URL (상대 경로)", 
            example = "/images/2025/09/12/8b12cca1-92d2-40ff-9fe7-940b01fd0fdc.jfif")
    private String userImageUrl;
}