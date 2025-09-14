package com.lgcns.beinstagramclone.user.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
// @Table(name = "INSPIRE_USER_TBL")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"blogs"})
public class UserEntity {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
    */

    @Id
    private String email    ;

    @Column(unique = true, nullable = false, length = 50)
    private String passwd   ;

    @Column(nullable = false, length = 50)
    private String name     ;

    @Column(length = 500)
    private String userImageUrl;

    @OneToMany(mappedBy="author",cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<PostEntity> posts = new ArrayList<>();

    // @OneToMany(mappedBy = "author", orphanRemoval = false)
    // private List<CommentEntity> comments = new ArrayList<>();
}
