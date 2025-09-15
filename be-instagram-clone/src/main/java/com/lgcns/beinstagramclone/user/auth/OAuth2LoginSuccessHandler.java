package com.lgcns.beinstagramclone.user.auth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    // ⭐ 여기에 JWT 토큰을 발급하는 로직을 추가할 거야.
    // 예를 들어, JWTService를 주입받아 토큰을 생성할 수 있어.

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        
        // ⭐ 1. authentication 객체에서 사용자 정보를 가져와요.
        //     (예: OAuth2User user = (OAuth2User) authentication.getPrincipal();)
        
        // ⭐ 2. 가져온 정보로 JWT Access Token과 Refresh Token을 생성해요.
        //     (예: String accessToken = jwtService.createAccessToken(user);)
        
        // ⭐ 3. 생성된 토큰을 클라이언트에 전달해요.
        //     (예: response.addHeader("Authorization", "Bearer " + accessToken);)
        
        // ⭐ 4. 클라이언트로 리다이렉트해요.
        response.sendRedirect("http://localhost:5173/main");
    }
}