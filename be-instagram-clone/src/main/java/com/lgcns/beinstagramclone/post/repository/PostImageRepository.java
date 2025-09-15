package com.lgcns.beinstagramclone.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.beinstagramclone.post.domain.entity.PostImageEntity;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Integer> {
}
