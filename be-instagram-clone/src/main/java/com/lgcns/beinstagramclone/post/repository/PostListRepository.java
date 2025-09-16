package com.lgcns.beinstagramclone.post.repository;

import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.repository.projection.PostSummaryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostListRepository extends JpaRepository<PostEntity, Long> {

    // 팔로우한 회원의 피드 조회
    @Query("""
        select 
            p.postID as postId,
            u.name   as authorName,
            p.content as content,
            p.createDate as createdAt
        from PostEntity p
        join p.author u
        where exists (
            select 1
            from com.lgcns.beinstagramclone.follow.domain.entity.FollowEntity f
            where f.follower.email = :myEmail
              and f.following = p.author
        )
        order by p.createDate desc, p.postID desc
    """)
    Slice<PostSummaryView> findFollowedFeedSummary(@Param("myEmail") String myEmail, Pageable pageable);
}