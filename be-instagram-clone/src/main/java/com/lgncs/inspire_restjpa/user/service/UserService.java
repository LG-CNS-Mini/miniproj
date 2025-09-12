package com.lgncs.inspire_restjpa.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgncs.inspire_restjpa.user.domain.dto.UserRequestDTO;
import com.lgncs.inspire_restjpa.user.domain.dto.UserResponseDTO;
import com.lgncs.inspire_restjpa.user.domain.entity.UserEntity;
import com.lgncs.inspire_restjpa.user.repository.RefreshTokenRepository;
import com.lgncs.inspire_restjpa.user.repository.UserRepository;
import com.lgncs.inspire_restjpa.util.JwtProvider;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository ;

    
    @Autowired
    private JwtProvider provider ; 

    public UserResponseDTO signup(UserRequestDTO request) {
        System.out.println(">>> service signup");
        UserEntity entity = userRepository.save(request.toEntity());
        return UserResponseDTO.fromEntity(entity) ;
    }

    @Transactional
    public Map<String, Object> signin(UserRequestDTO request) {
        System.out.println(">>> service signin");
        
        UserEntity entity = 
            userRepository.findByEmailAndPasswd(request.getEmail(), request.getPasswd()) ;
        
        // 토근 생성을 위해서 사용자 기본키(email)를 전달
        String accToken = provider.generateAccessToken(request.getEmail())  ;
        String refToken = provider.generateRefreshToken(request.getEmail()) ;

        // redis refresh token 저장 
        long refreshTokenExpiry = provider.getRefreshTokenExpiry(); // ex: 7일
        refreshTokenRepository.save(request.getEmail(), 
                                    refToken, 
                                    refreshTokenExpiry);
        
        System.out.println(">>> service signin redis save"); 

        UserResponseDTO response = UserResponseDTO.fromEntity(entity) ; 
        // response.setAccessToken(accToken);
        // response.setRefreshToken(refToken);

        Map<String , Object> map = new HashMap<>();
        map.put("response", response);
        map.put("access"  , accToken);
        map.put("refresh" , refToken);
        
        return map ;  
                
    }

    public void logout(String email) {
        System.out.println(">>> service logout redis delete"); 
        // Redis에서 Refresh Token 제거
        refreshTokenRepository.delete(email); 
    
    }

}
