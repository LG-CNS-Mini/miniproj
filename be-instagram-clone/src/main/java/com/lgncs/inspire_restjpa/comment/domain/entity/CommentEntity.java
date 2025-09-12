package com.lgncs.inspire_restjpa.comment.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lgncs.inspire_restjpa.blog.domain.entity.BlogEntity;
import com.lgncs.inspire_restjpa.user.domain.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@ToString(exclude = {"blog"} ) 
public class CommentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer commentId ; 

    @Column(nullable = false , length = 500)
    private String comment ; 

    @ManyToOne(fetch = FetchType.LAZY, optional = false) 
    @JoinColumn(name = "blog_id")
    @JsonBackReference
    private BlogEntity blog ; 


    // @ManyToOne(fetch = FetchType.LAZY, optional = false) 
    // @JoinColumn(name = "author_email")
    // @JsonBackReference
    // private UserEntity author ; 


}
