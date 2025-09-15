package com.lgcns.beinstagramclone.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Table(
    name = "follow",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_follow_pair", columnNames = {"follower_id", "following_id"})
    },
    indexes = {
        @Index(name = "idx_follow_follower",  columnList = "follower_id"),
        @Index(name = "idx_follow_following", columnList = "following_id")
    }
)
public class Follow {

    @EmbeddedId
    private FollowId id;  // (follower_id, following_id)

    // follower_id = user_entity.email
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("followerId")
    @JoinColumn(
        name = "follower_id",
        referencedColumnName = "email",
        foreignKey = @ForeignKey(name = "fk_follow_follower")
    )
    private UserEntity follower;

    // following_id = user_entity.email
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("followingId")
    @JoinColumn(
        name = "following_id",
        referencedColumnName = "email",
        foreignKey = @ForeignKey(name = "fk_follow_following")
    )
    private UserEntity following;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
    @Embeddable
    public static class FollowId implements Serializable {
        @Column(name = "follower_id",  length = 255, nullable = false)
        private String followerId;   // 팔로워 이메일

        @Column(name = "following_id", length = 255, nullable = false)
        private String followingId;  // 팔로잉 대상 이메일
    }
}
