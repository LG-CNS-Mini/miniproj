package com.lgcns.beinstagramclone.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lgcns.beinstagramclone.like.domain.entity.LikeEntity;
import com.lgcns.beinstagramclone.like.domain.entity.LikeId;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.query.Param;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, LikeId> {
    // email과 postId를 사용하여 좋아요 기록이 있는지 확인 (boolean 반환)
    public boolean existsByEmailAndPostId(String email, int postId);
    
    // 특정 게시물(postId)의 좋아요 총 개수 조회
    public int countByPostId(int postId);

    // 게시물 별 좋아요 개수 조회
    @Query("""
        select l.postId as postId, count(l) as cnt
        from LikeEntity l
        where l.postId in :postIds
        group by l.postId
    """)
    List<PostLikeCount> countByPostIds(@Param("postIds") Collection<Integer> postIds);

    // 특정 회원이 좋아요한 글 목록 조회
    @Query("""
        select l.postId
        from LikeEntity l
        where l.email = :email
          and l.postId in :postIds
    """)
    List<Integer> findMyLikedPostIds(@Param("postIds") Collection<Integer> postIds,
                                     @Param("email") String email);

    
    interface PostLikeCount {
        Integer getPostId();
        long getCnt();

    }
}


