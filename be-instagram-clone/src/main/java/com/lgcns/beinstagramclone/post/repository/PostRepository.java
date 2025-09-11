package com.lgcns.beinstagramclone.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Integer> {
    
}
