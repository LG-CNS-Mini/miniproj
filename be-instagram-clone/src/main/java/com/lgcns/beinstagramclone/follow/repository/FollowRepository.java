package com.lgcns.beinstagramclone.follow.repository;

import com.lgcns.beinstagramclone.follow.domain.entity.FollowEntity;
import com.lgcns.beinstagramclone.follow.domain.entity.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, FollowId> {

    boolean existsById(FollowId id);

    void deleteById(FollowId id);
}
