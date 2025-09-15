package com.lgcns.beinstagramclone.follow.domain.entity;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowEntity {

    @EmbeddedId
    private FollowId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followerEmail") // FollowId.followerEmail과 매핑
    @JoinColumn(name = "follower_email")
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("followingEmail") // FollowId.followingEmail과 매핑
    @JoinColumn(name = "following_email")
    private UserEntity following;
}
