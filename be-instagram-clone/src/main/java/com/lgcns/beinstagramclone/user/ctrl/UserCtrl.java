package com.lgcns.beinstagramclone.user.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.user.domain.dto.UserRequestDTO;
import com.lgcns.beinstagramclone.user.domain.dto.UserResponseDTO;
import com.lgcns.beinstagramclone.user.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;



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
        
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getAllErrors().forEach(err -> {
                FieldError filed = (FieldError) err;
                String msg = err.getDefaultMessage();
                System.out.println(">>> validation err : "+filed.getField()+" - "+msg);
                errorMap.put(filed.getField(), msg);
            });
            // err - 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
        }

        UserResponseDTO response = userService.signup(request);
        
        if(response != null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 인증, 인가 : cookie, session, jwt token
    
    // 인증(Authentication) : 누구인지 확인하는 절차 ,
    // Bearer token - JWT 기반 인증, OAuth2
    // token(accessToken, refreshToken)
    // 응답시(body X, header O) : 형태) Authorization: Bearer <token>

    // 인가(Authorization)  : 권한부여( endpoint 접근권한 )
    // 요청시(header 응답시 전송한 Bearer token 유무를 체크하고 접근권한 확인)
    @GetMapping("/signin")
    //public ResponseEntity<UserResponseDTO> signin(@RequestParam @Valid UserRequestDTO request) {
    public ResponseEntity<UserResponseDTO> signin(UserRequestDTO request) {
        System.out.println(">>> user ctrl GET /signin");
        System.out.println(">>> user ctrl GET /signin param" + request);
        Map<String, Object> map = userService.signin(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Authorization", "Bearer "+(String)(map.get("access")))
                .header("Refresh-Token", (String)(map.get("refresh")))
                .body((UserResponseDTO)(map.get("response")));
    }
    
    
}
