package com.lgcns.beinstagramclone.Image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer>{
    
}