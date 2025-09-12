package com.lgncs.inspire_restjpa.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lgncs.inspire_restjpa.user.domain.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{

    public UserEntity findByEmailAndPasswd(String email, String passwd);

}


