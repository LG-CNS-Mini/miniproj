package com.lgcns.beinstagramclone.user.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.user.repository.RefreshTokenRepository;
import com.lgcns.beinstagramclone.util.JwtProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth/api/v2/inspire/user")
@Tag(name = "Logout API", description = "로그아웃(Refresh Token 제거)")
public class LogoutCtrl {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository ;

    @Autowired
    private JwtProvider provider ; 
    
    
    @PostMapping("/logout")
     @Operation(
        summary = "로그아웃",
        description = "Authorization 헤더의 Access Token에서 사용자 식별자를 추출하고, 해당 사용자의 Refresh Token을 Redis에서 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "로그아웃 완료",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰 / 헤더 누락",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<Void> logout(@RequestHeader("Authorization")
            @Parameter(
            in = ParameterIn.HEADER,
            name = "Authorization",
            required = true,
            description = "Bearer 액세스 토큰",
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            String token) {
        String email = provider.getUserIdFromToken(token); // accessToken에서 userId 추출
        refreshTokenRepository.delete(email); // Redis에서 Refresh Token 삭제
        return ResponseEntity.ok().build();
    }
}
