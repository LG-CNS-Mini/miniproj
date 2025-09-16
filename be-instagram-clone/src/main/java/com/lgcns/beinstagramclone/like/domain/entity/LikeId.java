package com.lgcns.beinstagramclone.like.domain.entity;

import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LikeId implements Serializable {
    private String email;
    private int postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeId likeId = (LikeId) o;
        return postId == likeId.postId && Objects.equals(email, likeId.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, postId);
    }
}