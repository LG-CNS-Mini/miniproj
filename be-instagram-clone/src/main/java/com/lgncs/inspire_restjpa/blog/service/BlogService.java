package com.lgncs.inspire_restjpa.blog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgncs.inspire_restjpa.blog.domain.dto.BlogRequestDTO;
import com.lgncs.inspire_restjpa.blog.domain.dto.BlogResponseDTO;
import com.lgncs.inspire_restjpa.blog.domain.entity.BlogEntity;
import com.lgncs.inspire_restjpa.blog.repository.BlogRepository;
import com.lgncs.inspire_restjpa.comment.domain.dto.CommentResponseDTO;
import com.lgncs.inspire_restjpa.user.domain.entity.UserEntity;
import com.lgncs.inspire_restjpa.user.repository.UserRepository;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository ; 

    @Autowired
    private UserRepository userRepository ; 

    public List<BlogResponseDTO> select() {
        System.out.println("[debug] >>> blog service select "); 
        List<BlogEntity> list = blogRepository.findAll() ;
        return list.stream()
                .map(entity -> BlogResponseDTO.builder()
                                .blogId(entity.getBlogId())
                                .title(entity.getTitle())
                                .content(entity.getContent())
                                .build())
                .toList() ; 
    }

    public BlogResponseDTO insert(BlogRequestDTO request) {
        System.out.println("[debug] >>> blog service insert "); 
        Optional<UserEntity> user =  
            userRepository.findById(request.getAuthorEmail());
        
        BlogEntity blog = blogRepository.save(
            BlogEntity.builder()
                .title(request.getTitle())
                .content(request.getContent()) 
                .author(user.get())
                .build()
        );
        return BlogResponseDTO.builder()
                .title(blog.getTitle())
                .content(blog.getContent()) 
                .authorEmail(blog.getAuthor().getEmail())
                .blogId(blog.getBlogId())
                .build(); 
    }  

    public BlogResponseDTO findBlog(Integer blogId) {
        System.out.println("[debug] >>> blog service findBlog "); 
        BlogEntity blogEntity = 
            blogRepository.findById(blogId)
                .orElseThrow(() -> 
                    new RuntimeException("해당 블로그 존재하지 않음"));
        System.out.println(">>>> service blogEntity ");
        System.out.println(blogEntity);
        
        // List<CommentResponseDTO> commentList = 
        //     blogEntity.getComments()
        //         .stream()
        //         .map( e -> CommentResponseDTO.fromEntity(e) )
        //         .toList(); 
        // System.out.println(">>>> service comment list "); 
        // commentList.forEach(System.out::println); 

        BlogResponseDTO response = 
            BlogResponseDTO.fromEntity(blogEntity);
        // response.setComments(commentList) ; 
        return response ;

    }

}
