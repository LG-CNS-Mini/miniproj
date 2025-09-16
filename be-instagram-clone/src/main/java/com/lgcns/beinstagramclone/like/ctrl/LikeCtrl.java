package com.lgcns.beinstagramclone.like.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.like.domain.dto.LikeRequestDTO;
import com.lgcns.beinstagramclone.like.domain.dto.LikeResponseDTO;
import com.lgcns.beinstagramclone.like.service.LikeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v2/inspire/like")
public class LikeCtrl {
       @Autowired
    private LikeService likeService;

    // 좋아요 상태 및 개수 조회
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
        @RequestParam String email,
        @RequestParam int postId) {
        
        // 유효성 검사는 서비스 레이어나 별도의 로직으로 옮길 수 있습니다.
        
        LikeResponseDTO response = likeService.getLikeStatus(email, postId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("isLiked", response.isLiked());
        result.put("likeCount", response.getLikeCount());
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
    // 좋아요
    @PostMapping("/like")
    public ResponseEntity<Map<String, Integer>> userLikesPost(
        @RequestBody @Valid LikeRequestDTO request,
        BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        LikeResponseDTO response = likeService.userlikesPost(request);
        
        Map<String, Integer> result = new HashMap<>();
        result.put("likeCount", response.getLikeCount());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 좋아요 취소
    @PostMapping("/unlike")
    public ResponseEntity<Map<String, Integer>> userUnlikesPost(
        @RequestBody @Valid LikeRequestDTO request,
        BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        LikeResponseDTO response = likeService.userUnlikesPost(request); 
        
        Map<String, Integer> result = new HashMap<>();
        result.put("likeCount", response.getLikeCount());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //싫어요
    @PostMapping("/dislike")
    public String userDislikePost(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
