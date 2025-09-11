package com.lgcns.beinstagramclone.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity,Integer> {
    
}
