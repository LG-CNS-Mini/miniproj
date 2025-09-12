package com.lgncs.inspire_restjpa.user.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgncs.inspire_restjpa.user.repository.RefreshTokenRepository;
import com.lgncs.inspire_restjpa.util.JwtProvider;

@RestController
@RequestMapping("auth/api/v2/inspire/user")
public class LogoutCtrl {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository ;

    @Autowired
    private JwtProvider provider ; 
    
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        String email = provider.getUserIdFromToken(token); // accessToken에서 userId 추출
        refreshTokenRepository.delete(email); // Redis에서 Refresh Token 삭제
        return ResponseEntity.ok().build();
    }
}
