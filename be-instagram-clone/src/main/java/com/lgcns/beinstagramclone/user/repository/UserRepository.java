package com.lgcns.beinstagramclone.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{

    public UserEntity findByEmailAndPasswd(String email, String passwd);

    public UserEntity findByEmail(String email);
}
