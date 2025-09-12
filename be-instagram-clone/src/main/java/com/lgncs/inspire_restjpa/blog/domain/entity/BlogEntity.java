package com.lgncs.inspire_restjpa.blog.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lgncs.inspire_restjpa.comment.domain.entity.CommentEntity;
import com.lgncs.inspire_restjpa.user.domain.entity.UserEntity;

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
@ToString(exclude = {"comments"}) 
public class BlogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blogId ; 
    
    @Column(nullable = false , length = 150)
    private String  title ; 
    
    @Column(nullable = false , length = 1000)
    private String  content ;

    // 외래키 설정 
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "author_email") 
    @JsonBackReference
    private UserEntity author ;

    @OneToMany(
        mappedBy = "blog" , 
        cascade = CascadeType.ALL , 
        orphanRemoval = true ) 
    @JsonManagedReference
    private List<CommentEntity> comments = new ArrayList<>();
    

}
