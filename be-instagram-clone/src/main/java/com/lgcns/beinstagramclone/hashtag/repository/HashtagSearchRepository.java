package com.lgcns.beinstagramclone.hashtag.repository;

import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface HashtagSearchRepository extends JpaRepository<PostEntity, Integer> {


    @Query("""
        select new com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO(
            p.author.name,
            p.content,
            p.createDate
        )
        from PostEntity p
            join p.tags pt
            join pt.hashtag h
        where h.name = :tag
        order by p.createDate desc, p.postID desc
    """)
    Slice<PostListItemDTO> findByHashtag(@Param("tag") String tag, Pageable pageable);

    // 다중 해시태그(any-match) 검색
    @Query("""
        select new com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO(
            p.author.name,
            p.content,
            p.createDate
        )
        from PostEntity p
        where exists (
            select 1
            from PostHashtagEntity pt
                join pt.hashtag h
            where pt.post = p
              and h.name in :tags
        )
        order by p.createDate desc, p.postID desc
    """)
    Slice<PostListItemDTO> findByAnyHashtags(@Param("tags") java.util.List<String> tags, Pageable pageable);
}
