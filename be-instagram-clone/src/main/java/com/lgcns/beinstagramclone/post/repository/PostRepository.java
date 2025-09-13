package com.lgcns.beinstagramclone.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    @Query("""
        select distinct p
        from PostEntity p
        left join fetch p.images pi
        left join fetch pi.image i
        order by p.createDate desc, pi.sortOrder asc
    """)
    List<PostEntity> findAllWithImages();

    @Query("""
        select p
        from PostEntity p
        left join fetch p.images pi
        left join fetch pi.image i
        where p.postID = :postId
        order by pi.sortOrder asc
    """)
    Optional<PostEntity> findByIdWithImages(@Param("postId") Integer postId);
}
