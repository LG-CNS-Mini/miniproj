package com.lgncs.inspire_restjpa.comment.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgncs.inspire_restjpa.comment.domain.dto.CommentRequestDTO;
import com.lgncs.inspire_restjpa.comment.domain.dto.CommentResponseDTO;
import com.lgncs.inspire_restjpa.comment.service.CommentService;

@RestController
@RequestMapping("/auth/api/v2/blog/comment")
public class CommentCtrl {

    @Autowired
    private CommentService commentService ;

    @PostMapping("/register")
    public ResponseEntity<List<CommentResponseDTO>> register(
            @RequestBody CommentRequestDTO request) { 

        System.out.println("[debug] >>> blog comment ctrl path POST : /register ");
        System.out.println("[debug] param dto = "+request);
        
        List<CommentResponseDTO> comments = commentService.insert(request); 
        if( comments.size() != 0 ) {
            return ResponseEntity.status(HttpStatus.CREATED).body(comments) ;  
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }      
    }

    @DeleteMapping("/delete/{blogId}/{commentId}") 
    public ResponseEntity<Void> delete(
        @PathVariable("blogId") Integer blogId,
        @PathVariable("commentId") Integer commentId) {
        System.out.println("[debug] >>> blog comment ctrl path DELETE : /delete ");
        System.out.println("[debug] param blogId = "+blogId); 
        System.out.println("[debug] param commentId = "+commentId); 
        commentService.delete(blogId, commentId); 
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null) ;  
    }
}
