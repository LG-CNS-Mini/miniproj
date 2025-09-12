package com.lgncs.inspire_restjpa.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lgncs.inspire_restjpa.blog.domain.entity.BlogEntity;

public interface BlogRepository extends JpaRepository<BlogEntity, Integer>{
}



