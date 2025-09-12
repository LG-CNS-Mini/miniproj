package com.lgncs.inspire_restjpa.blog.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgncs.inspire_restjpa.blog.domain.dto.BlogRequestDTO;
import com.lgncs.inspire_restjpa.blog.domain.dto.BlogResponseDTO;
import com.lgncs.inspire_restjpa.blog.service.BlogService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth/api/v2/blog")
public class BlogCtrl {

    @Autowired
    private BlogService blogService ; 
    
    @GetMapping("/blogs")
    public ResponseEntity<List<BlogResponseDTO>> blogs() {
        System.out.println("[debug] >>> blog ctrl path : /blogs ");
        
        List<BlogResponseDTO> list = blogService.select();

        return new ResponseEntity<List<BlogResponseDTO>>(list, HttpStatus.OK) ; 
        
    }
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody BlogRequestDTO request) { 

        System.out.println("[debug] >>> blog ctrl path POST : /register ");
        System.out.println("[debug] param dto = "+request);
        
        BlogResponseDTO response = blogService.insert(request); 
        if( response != null  ) {
            return ResponseEntity.status(HttpStatus.CREATED).body(null) ; 
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }      
        
    }

    @GetMapping("/read/{blogId}")
    public ResponseEntity<BlogResponseDTO> read(
                    @PathVariable("blogId") Integer blogId) {

        System.out.println("[debug] >>> blog ctrl path GET : /read ");
        System.out.println("[debug] param blog id = "+blogId);
        BlogResponseDTO response = blogService.findBlog(blogId); 
        System.out.println("[debug] result response = "+response ) ; 
                       
        if( response != null ) {
            return new ResponseEntity<>(response , HttpStatus.OK); // 200
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 
        }  
    }

    

    

}



