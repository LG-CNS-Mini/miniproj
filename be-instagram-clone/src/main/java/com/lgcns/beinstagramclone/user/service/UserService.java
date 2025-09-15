package com.lgcns.beinstagramclone.user.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lgcns.beinstagramclone.user.domain.dto.UserRequestDTO;
import com.lgcns.beinstagramclone.user.domain.dto.UserResponseDTO;
import com.lgcns.beinstagramclone.user.domain.dto.UserSuggestDTO;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.RefreshTokenRepository;
import com.lgcns.beinstagramclone.user.repository.UserRepository;
import com.lgcns.beinstagramclone.util.JwtProvider;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtProvider provider;

    public UserResponseDTO signup(UserRequestDTO request) {
        System.out.println(">>> service signup");
        UserEntity entity = userRepository.save(request.toEntity());
        return UserResponseDTO.fromEntity(entity);
    }

    public Map<String, Object> signin(UserRequestDTO request) {
        System.out.println(">>> service signin");
        UserEntity entity = userRepository.findByEmailAndPasswd(request.getEmail(), request.getPasswd());

        // 토큰 생성을 위해서 사용자 기본키(email)를 전달.
        String accToken = provider.generateAccessToken(request.getEmail());
        String refToken = provider.generateRefreshToken(request.getEmail());

        UserResponseDTO response = UserResponseDTO.fromEntity(entity);
        // response.setAccessToken(accToken);
        // response.setRefreshToken(refToken);

        Map<String, Object> map = new HashMap<>();
        map.put("response", response);
        map.put("access", accToken);
        map.put("refresh", refToken);

        refreshTokenRepository.save(accToken, refToken, provider.getREFRESH_TOKEN_EXPIRY());

        return map;
    }

    public void logout(String email) {
        System.out.println(">>> service logout redis delete");
        // Redis에서 Refresh Token 제거
        refreshTokenRepository.delete(email);

    }

    public List<UserSuggestDTO> suggestUsers(String keyword, int limit) {
        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by("name").ascending().and(Sort.by("nickname").ascending()));

        String kw = (keyword == null ? "" : keyword).trim();

        return userRepository
                .searchByNameOrNickname(kw, pageable)
                .stream()
                .map(u -> UserSuggestDTO.builder()
                        .email(u.getEmail())
                        .name(u.getName())
                        .nickname(u.getNickname())
                        .userImageUrl(u.getUserImageUrl())
                        .build())
                .toList();

    }
}
