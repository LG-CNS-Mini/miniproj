package com.lgcns.beinstagramclone.post.domain.entity;

import java.time.LocalDateTime;

import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postID;

    @Column(nullable = false, length = 2000)  
    private String content;

    @Column(length = 255)  
    private String hashtag;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_email")
    private UserEntity author;

    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }
}
