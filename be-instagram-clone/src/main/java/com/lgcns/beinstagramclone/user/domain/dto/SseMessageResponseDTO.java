package com.lgcns.beinstagramclone.user.domain.dto;

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
public class SseMessageResponseDTO {
    private String title;
    private String content;
    private String type; // 예: login, comment, notice 등
    private String link; // 클릭 시 이동할 URL 또는 ID
}
