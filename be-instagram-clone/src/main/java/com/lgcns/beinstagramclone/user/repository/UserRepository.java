package com.lgcns.beinstagramclone.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    public UserEntity findByEmailAndPasswd(String email, String passwd);

    @Query("""
            SELECT u
            FROM UserEntity u
            WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :kw, '%'))
               OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :kw, '%'))
            """)
    Page<UserEntity> searchByNameOrNickname(@Param("kw") String keyword, Pageable pageable);

    Optional<UserEntity> findByEmail(String email);
}
