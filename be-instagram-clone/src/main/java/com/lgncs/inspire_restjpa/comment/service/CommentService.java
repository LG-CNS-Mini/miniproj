package com.lgncs.inspire_restjpa.comment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgncs.inspire_restjpa.blog.domain.dto.BlogResponseDTO;
import com.lgncs.inspire_restjpa.blog.domain.entity.BlogEntity;
import com.lgncs.inspire_restjpa.blog.repository.BlogRepository;
import com.lgncs.inspire_restjpa.comment.domain.dto.CommentRequestDTO;
import com.lgncs.inspire_restjpa.comment.domain.dto.CommentResponseDTO;
import com.lgncs.inspire_restjpa.comment.domain.entity.CommentEntity;
import com.lgncs.inspire_restjpa.comment.repository.CommentRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository ; 

    @Autowired
    private BlogRepository blogRepository ;
    
    @Transactional
    public List<CommentResponseDTO> insert(CommentRequestDTO request) {
        System.out.println("[debug] >>> comment service insert comment ");
        BlogEntity blog = blogRepository.findById(request.getBlogId())
                .orElseThrow(() -> new RuntimeException("블로그가 존재하지 않습니다. ID=" + request.getBlogId()));

        CommentEntity comment = CommentEntity.builder()
            .comment(request.getComment())
            .blog(blog)
            .build();
        blog.getComments().add( comment) ; 

        commentRepository.save(comment);
        
        List<CommentEntity> allComments 
            = commentRepository.findByBlog_BlogId(request.getBlogId()) ;
        return allComments.stream()
            .map(e -> CommentResponseDTO.fromEntity(e))
            .toList() ;
         
    }


    public void delete(Integer blogId, Integer commentId) {

        System.out.println("[debug] >>> comment service delete comment ");
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다. ID=" + commentId));
        
        if (!comment.getBlog().getBlogId().equals(blogId)) {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        commentRepository.delete(comment);

    }
}
