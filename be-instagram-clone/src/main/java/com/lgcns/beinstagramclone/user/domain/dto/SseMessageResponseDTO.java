package com.lgcns.beinstagramclone.user.domain.dto;

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
@Schema(description = "서버 알림 메시지 응답 DTO (SSE 용)")
public class SseMessageResponseDTO {

    @Schema(description = "알림 제목", example = "새 댓글 알림")
    private String title;

    @Schema(description = "알림 내용", example = "홍길동 님이 내 게시글에 댓글을 달았습니다.")
    private String content;

    @Schema(description = "알림 타입 (login, comment, notice 등)", example = "comment")
    private String type; // 예: login, comment, notice 등

    @Schema(description = "알림 클릭 시 이동할 링크 (URL 또는 리소스 ID)", example = "/posts/11")
    private String link; // 클릭 시 이동할 URL 또는 ID
}