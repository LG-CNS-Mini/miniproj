package com.lgcns.beinstagramclone.user.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;
import com.lgcns.beinstagramclone.Image.repository.ImageRepository;
import com.lgcns.beinstagramclone.user.domain.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.RefreshTokenRepository;
import com.lgcns.beinstagramclone.user.repository.UserRepository;
import com.lgcns.beinstagramclone.util.JwtProvider;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtProvider provider;

    @Value("${file.upload-dir}")
    private String uploadDir;

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

    public UserProfileResponseDTO selectUser(String userEmail, String authorEmail) {
        return userRepository.selectUser(userEmail, authorEmail);
    }

    @Transactional
    public ProfileImageResponseDTO updateProfileImage(String userEmail, MultipartFile image){
        String imageUrl = saveFileAndReturnPublicUrl(image);
        UserEntity entity = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("유저 존재하지 않음"));
        entity.setUserImageUrl(imageUrl);
        userRepository.save(entity);
        return new ProfileImageResponseDTO(imageUrl);
    }

    private String saveFileAndReturnPublicUrl(MultipartFile file) {
        try {
            String original = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
            String ext = original.contains(".") ? original.substring(original.lastIndexOf('.') + 1) : "bin";

            LocalDate today = LocalDate.now();
            Path dir = Paths.get(uploadDir,
                    String.valueOf(today.getYear()),
                    String.format("%02d", today.getMonthValue()),
                    String.format("%02d", today.getDayOfMonth()));

            // 디렉토리 생성 (없으면 자동 생성)
            Files.createDirectories(dir);

            String filename = UUID.randomUUID() + "." + ext;
            Path saved = dir.resolve(filename);
            file.transferTo(saved.toFile());

            // URL 반환 (정적 리소스 핸들러랑 맞춰줘야 함)
            return "/images/" + today.getYear() + "/"
                    + String.format("%02d", today.getMonthValue()) + "/"
                    + String.format("%02d", today.getDayOfMonth()) + "/"
                    + filename;
        } catch (Exception e) {
            throw new RuntimeException("이미지 저장 실패: " + file.getOriginalFilename(), e);
        }
    }
}
