package com.lgcns.beinstagramclone.user.ctrl;

import com.lgcns.beinstagramclone.user.domain.dto.*;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.service.UserService;
import com.nimbusds.oauth2.sdk.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User API", description = "유저 관련 API")
public class UserCtrl {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @Operation(
      summary = "회원가입",
      responses = {
        @ApiResponse(
          responseCode = "204",
          description = "가입 성공",
          content = @Content 
        ),
        @ApiResponse(
          responseCode = "400",
          description = "유효성 검증 실패",
          content = @Content
        ),
        @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content
        )
      }
    )
    public ResponseEntity signup(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "회원가입 요청 본문",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = UserRequestDTO.class))
            )
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
    @Operation(
        summary = "로그인",
        description = "이메일/비밀번호로 로그인하고, 응답 헤더에 액세스/리프레시 토큰을 반환합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "로그인 성공",
                headers = {
                    @Header(name = "Authorization", description = "Bearer <access token>", schema = @Schema(type = "string")),
                    @Header(name = "Refresh-Token", description = "Refresh token", schema = @Schema(type = "string"))
                },
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class))
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    // public ResponseEntity<UserResponseDTO> signin(@RequestParam @Valid
    // UserRequestDTO request) {
    public ResponseEntity<UserResponseDTO> signin(  
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        description = "로그인 요청",
        content = @Content(schema = @Schema(implementation = UserRequestDTO.class)))
        UserRequestDTO request) {
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
    @Operation(
        summary = "사용자 자동완성",
        description = "이름/이메일 일부 키워드로 사용자 목록을 최대 n개까지 추천합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserSuggestDTO.class))))
        }
    )
    public ResponseEntity<List<UserSuggestDTO>> suggestUsers(
            @Parameter(description = "검색 키워드(부분 일치).", example = "jun") 
            @RequestParam(name = "keyword", required = false) String keyword,
            @Parameter(description = "최대 반환 개수", example = "10")
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(userService.suggestUsers(keyword, limit));
    }

    @GetMapping("/{userEmail}/{authorEmail}")
    @Operation(
        summary = "프로필 조회",
        description = "요청자가 특정 사용자의 프로필을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserProfileResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "사용자 없음",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<UserProfileResponseDTO> getUser(
            @Parameter(description = "조회 대상 사용자 이메일", example = "target@example.com", required = true)
            @PathVariable("userEmail") String userEmail,
            @Parameter(description = "요청자(작성자) 이메일", example = "me@example.com", required = true)
            @PathVariable("authorEmail") String authorEmail) {
        return ResponseEntity.ok().body(userService.selectUser(userEmail, authorEmail));
    }

    
    @PostMapping("/profileimage/{userId}")
    @Operation(
        summary = "프로필 이미지 업데이트",
        description = "단일 이미지 파일을 업로드하여 사용자 프로필 이미지를 변경합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProfileImageResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청/파일 누락",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "415", description = "지원하지 않는 콘텐츠 타입",
                content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                content = @Content(schema = @Schema(hidden = true)))
        }
    )
    public ResponseEntity<ProfileImageResponseDTO> updateProfileImage(
            @Parameter(description = "사용자 ID", example = "user_123", required = true)
            @PathVariable("userId") String userId,
            @Parameter(
                description = "업로드할 이미지 파일 (multipart/form-data)",
                content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(type = "string", format = "binary"))
            )
            @RequestParam("image") MultipartFile image) {
        try {
            return ResponseEntity.ok(userService.updateProfileImage(userId, image));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}