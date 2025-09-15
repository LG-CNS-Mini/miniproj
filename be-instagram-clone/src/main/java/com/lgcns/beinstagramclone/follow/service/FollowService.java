package com.lgcns.beinstagramclone.follow.service;

import com.lgcns.beinstagramclone.follow.domain.entity.FollowEntity;
import com.lgcns.beinstagramclone.follow.domain.entity.FollowId;
import com.lgcns.beinstagramclone.follow.repository.FollowRepository;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void follow(String followerEmail, String followingEmail) {
        UserEntity follower = userRepository.findByEmail(followerEmail)
                .orElseThrow(() -> new IllegalArgumentException("팔로워 없음"));
        UserEntity following = userRepository.findByEmail(followingEmail)
                .orElseThrow(() -> new IllegalArgumentException("팔로잉 없음"));

        FollowId id = new FollowId(followerEmail, followingEmail);

        if (followRepository.existsById(id)) {
            throw new IllegalStateException("이미 팔로우함");
        }

        FollowEntity follow = FollowEntity.builder()
                .id(id)
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(String followerEmail, String followingEmail) {
        FollowId id = new FollowId(followerEmail, followingEmail);
        followRepository.deleteById(id);
    }
}
