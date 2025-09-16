package com.lgcns.beinstagramclone.like.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgcns.beinstagramclone.like.domain.dto.LikeRequestDTO;
import com.lgcns.beinstagramclone.like.domain.dto.LikeResponseDTO;
import com.lgcns.beinstagramclone.like.domain.entity.LikeEntity;
import com.lgcns.beinstagramclone.like.domain.entity.LikeId;
import com.lgcns.beinstagramclone.like.repository.LikeRepository;

@Service
public class LikeService {
    
    @Autowired
    private LikeRepository likeRepository;

    public LikeResponseDTO getLikeStatus(String email, int postId) {
        // 복합 키 객체 생성
        LikeId likeId = new LikeId(email, postId);

        // existsById()를 사용하여 좋아요 기록 존재 여부 확인
        boolean isLiked = likeRepository.existsById(likeId);
        
        // 특정 게시물의 총 좋아요 개수 조회
        int likeCount = likeRepository.countByPostId(postId);

        return LikeResponseDTO.builder()
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }

    public LikeResponseDTO userlikesPost(LikeRequestDTO request) {
        // 좋아요 엔티티 생성
        LikeEntity like = LikeEntity.builder()
                                .email(request.getEmail())
                                .postId(request.getPostId())
                                .createDate(new Date())
                                .build();
        likeRepository.save(like);

        // 최신 좋아요 개수 조회
        int currentLikeCount = likeRepository.countByPostId(request.getPostId());

        return LikeResponseDTO.builder()
                .likeCount(currentLikeCount)
                .build();
    }

    public LikeResponseDTO userUnlikesPost(LikeRequestDTO request) {
        // 복합 키 객체 생성
        LikeId likeId = new LikeId(request.getEmail(), request.getPostId());

        // deleteById()를 사용하여 좋아요 기록 삭제
        likeRepository.deleteById(likeId);

        // 최신 좋아요 개수 조회
        int currentLikeCount = likeRepository.countByPostId(request.getPostId());
        
        return LikeResponseDTO.builder()
                .likeCount(currentLikeCount)
                .build();
    }
    // @Autowired 
    // private LikeRepository likeRepository;

    // public LikeResponseDTO userlikesPost(LikeRequestDTO requestDTO) {
    //     System.out.println("[debug] >>> user likes post (좋아요 클릭)");
    //     LikeEntity entity = likeRepository.save(requestDTO.toEntity());
    //     return LikeResponseDTO.fromEntity(entity);
    // }

}
