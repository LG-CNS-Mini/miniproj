package com.lgcns.beinstagramclone.comment.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "author", "post", "parent", "children" })
@EqualsAndHashCode(of = "commentId")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_email", nullable = false)
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private List<CommentEntity> children = new ArrayList<>();

    // 0 = 댓글, 1 = 대댓글
    @Min(0)
    @Max(1)
    @Column(name = "depth", nullable = false)
    private Integer depth;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;


    @PrePersist
    private void prePersistDefaults() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (depth == null)
            depth = 0;
        if (depth < 0)
            depth = 0;
        if (depth > 1)
            depth = 1;
    }
}