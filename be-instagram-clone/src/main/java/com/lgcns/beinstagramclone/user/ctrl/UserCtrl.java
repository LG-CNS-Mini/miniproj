package com.lgcns.beinstagramclone.user.ctrl;

import com.lgcns.beinstagramclone.user.domain.dto.*;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/inspire/user")
public class UserCtrl {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(
            @RequestBody @Valid UserRequestDTO request,
            BindingResult bindingResult) {
        System.out.println(">>> user ctrl POST /signup");
        System.out.println(">>> user ctrl POST /signup param" + request);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getAllErrors().forEach(err -> {
                FieldError filed = (FieldError) err;
                String msg = err.getDefaultMessage();
                System.out.println(">>> validation err : " + filed.getField() + " - " + msg);
                errorMap.put(filed.getField(), msg);
            });
            // err - 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        UserResponseDTO response = userService.signup(request);

        if (response != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 인증, 인가 : cookie, session, jwt token

    // 인증(Authentication) : 누구인지 확인하는 절차 ,
    // Bearer token - JWT 기반 인증, OAuth2
    // token(accessToken, refreshToken)
    // 응답시(body X, header O) : 형태) Authorization: Bearer <token>

    // 인가(Authorization) : 권한부여( endpoint 접근권한 )
    // 요청시(header 응답시 전송한 Bearer token 유무를 체크하고 접근권한 확인)
    @GetMapping("/signin")
    // public ResponseEntity<UserResponseDTO> signin(@RequestParam @Valid
    // UserRequestDTO request) {
    public ResponseEntity<UserResponseDTO> signin(UserRequestDTO request) {
        System.out.println(">>> user ctrl GET /signin");
        System.out.println(">>> user ctrl GET /signin param" + request);
        Map<String, Object> map = userService.signin(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", "Bearer " + (String) (map.get("access")))
                .header("Refresh-Token", (String) (map.get("refresh")))
                .body((UserResponseDTO) (map.get("response")));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<UserSuggestDTO>> suggestUsers(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(userService.suggestUsers(keyword, limit));
    }

    @GetMapping("/{userEmail}/{authorEmail}")
    public ResponseEntity<UserProfileResponseDTO> getUser(
            @PathVariable("userEmail") String userEmail,
            @PathVariable("authorEmail") String authorEmail) {
        return ResponseEntity.ok().body(userService.selectUser(userEmail, authorEmail));
    }

    @PostMapping("/profileimage/{userId}")
    public ResponseEntity<ProfileImageResponseDTO> updateProfileImage(
            @PathVariable("userId") String userId,
            @RequestParam("image") MultipartFile image) {
        try {
            return ResponseEntity.ok(userService.updateProfileImage(userId, image));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}