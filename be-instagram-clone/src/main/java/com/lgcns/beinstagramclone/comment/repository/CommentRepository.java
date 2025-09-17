package com.lgcns.beinstagramclone.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lgcns.beinstagramclone.comment.domain.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query("""
              select c from CommentEntity c
              join fetch c.author
              left join fetch c.parent
              where c.post.postID = :postId 
              order by c.createdAt asc
            """)
    List<CommentEntity> findAllByPostIdWithAuthor(@Param("postId") Integer postId);
    
}