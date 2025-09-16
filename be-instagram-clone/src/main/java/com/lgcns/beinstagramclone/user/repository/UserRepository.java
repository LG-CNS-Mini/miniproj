package com.lgcns.beinstagramclone.user.repository;

import java.util.Optional;

import com.lgcns.beinstagramclone.user.domain.dto.UserProfileResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
    SELECT new com.lgcns.beinstagramclone.user.domain.dto.UserProfileResponseDTO(
        u.email,
        u.name,
        u.userImageUrl,
        u.nickname,
        COUNT(DISTINCT follower.follower.email) AS followerCount,
        COUNT(DISTINCT following.following.email) AS followingCount,
        COUNT(DISTINCT p.postID) AS postCount,
        COUNT(DISTINCT isfollow.id) AS isFollow
    )
    FROM UserEntity u
    LEFT JOIN PostEntity p ON u.email = p.author.email
    LEFT JOIN FollowEntity follower ON u.email = follower.following.email
    LEFT JOIN FollowEntity following ON u.email = following.follower.email
    LEFT JOIN FollowEntity isfollow 
      ON u.email = isfollow.following.email
     AND isfollow.follower.email = :authorEmail
    WHERE u.email = :email
    GROUP BY u.email, u.name, u.userImageUrl, u.nickname
""")
UserProfileResponseDTO selectUser(@Param("email") String email, @Param("authorEmail") String authorEmail);
}