package com.lgcns.beinstagramclone.user.auth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.lgcns.beinstagramclone.user.repository.RefreshTokenRepository;
import com.lgcns.beinstagramclone.util.CustomOAuth2User;
import com.lgcns.beinstagramclone.util.JwtProvider;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    // ⭐ 여기에 JWT 토큰을 발급하는 로직을 추가할 거야.
    // 예를 들어, JWTService를 주입받아 토큰을 생성할 수 있어.

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info(">>> OAuth2LoginSuccessHandler bean is created!");
        System.out.println("[debug] >>> onAuthenticationSuccess start");
         // ⭐ 1. authentication 객체에서 사용자 정보를 가져와요.
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        // 사용자 이메일을 가져와요. 이 정보로 토큰을 만들 거예요!
        String email = customOAuth2User.getEmail();
        log.info(">>> OAuth2 Login Success for user: {}", email);
        
        // ⭐ 2. 가져온 정보로 JWT Access Token과 Refresh Token을 생성해요.
        // 이메일을 기준으로 토큰을 생성하는 것이 일반적이에요.
        String accessToken = jwtProvider.generateAccessToken(email);
        String refreshToken = jwtProvider.generateRefreshToken(email);
        
        // Redis에 Refresh Token 저장
        refreshTokenRepository.save(email, refreshToken, jwtProvider.getREFRESH_TOKEN_EXPIRY());
        
        // ⭐ 3. 생성된 토큰을 클라이언트에 전달해요.
        // CORS 정책에 따라 이 헤더들을 노출시켜야 해요!
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh-Token", refreshToken);

        // ⭐ 4. 클라이언트로 리다이렉트해요.
        // 프론트엔드가 토큰을 헤더에서 읽을 수 있도록 리다이렉트합니다.
        // 리다이렉트 URL에 토큰을 파라미터로 포함시켜도 좋아요.
        //String redirectUrl = "http://localhost:5173/main?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
        String redirectUrl = "http://localhost:5173/oauth2/redirect?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
        response.sendRedirect(redirectUrl);

        System.out.println("[debug] >>> onAuthenticationSuccess end");
    }
}