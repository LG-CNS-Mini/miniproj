package com.lgcns.beinstagramclone.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lgcns.beinstagramclone.like.domain.entity.LikeEntity;
import com.lgcns.beinstagramclone.like.domain.entity.LikeId;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, LikeId> {
    // email과 postId를 사용하여 좋아요 기록이 있는지 확인 (boolean 반환)
    public boolean existsByEmailAndPostId(String email, int postId);
    
    // 특정 게시물(postId)의 좋아요 총 개수 조회
    public int countByPostId(int postId);
}


