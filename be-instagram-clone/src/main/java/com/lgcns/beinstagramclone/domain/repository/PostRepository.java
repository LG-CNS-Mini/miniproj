package com.lgcns.beinstagramclone.domain.repository;

import com.lgcns.beinstagramclone.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.domain.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(
      value = """
              select new com.lgcns.beinstagramclone.domain.dto.PostListItemDTO(
                  u.name,         
                  p.content,       
                  p.hashtag,       
                  p.createDate     
              )
              from PostEntity p
              join p.author u
              order by p.createDate desc
              """,
      countQuery = "select count(p) from PostEntity p"
    )
    Page<PostListItemDTO> findLatestSummaries(Pageable pageable);

    @Query("""
        select new com.lgcns.beinstagramclone.domain.dto.PostListItemDTO(
            p.author.name,
            p.content,
            p.hashtag,
            p.createDate
        )
        from PostEntity p
        where exists (
            select 1
            from Follow f
            where f.follower.email = :myEmail
              and f.following = p.author
        )
        order by p.createDate desc, p.postId desc
    """)
    Slice<PostListItemDTO> findFollowedFeed(@Param("myEmail") String myEmail, Pageable pageable);

    // (옵션) 내 글도 포함하고 싶으면 아래 메서드 사용
    @Query("""
        select new com.lgcns.beinstagramclone.domain.dto.PostListItemDTO(
            p.author.name,
            p.content,
            p.hashtag,
            p.createDate
        )
        from PostEntity p
        where p.author.email = :myEmail
           or exists (
               select 1 from Follow f
               where f.follower.email = :myEmail
                 and f.following = p.author
           )
        order by p.createDate desc, p.postId desc
    """)
    Slice<PostListItemDTO> findFollowedFeedIncludingMe(@Param("myEmail") String myEmail, Pageable pageable);
}
