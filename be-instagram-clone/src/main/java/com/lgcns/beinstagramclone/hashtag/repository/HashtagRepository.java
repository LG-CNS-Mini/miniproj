package com.lgcns.beinstagramclone.hashtag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.beinstagramclone.hashtag.domain.entity.HashtagEntity;


public interface HashtagRepository extends JpaRepository<HashtagEntity, Integer> {
    Optional<HashtagEntity> findByName(String name);
}
