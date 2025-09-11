package com.lgcns.beinstagramclone.post.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgcns.beinstagramclone.post.domain.dto.PostRequestDTO;
import com.lgcns.beinstagramclone.post.domain.dto.PostResponseDTO;
import com.lgcns.beinstagramclone.post.service.PostService;

@RestController
@RequestMapping("/api/v1/post")
public class PostCtrl {

    @Autowired
    private PostService postService;


}
