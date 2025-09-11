package com.lgcns.beinstagramclone.post.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.post.service.PostService;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    
    @Autowired
    private PostService postService;
    
}
