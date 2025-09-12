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

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDTO>> posts() {
        List<PostResponseDTO> list = postService.select();
        return new ResponseEntity<List<PostResponseDTO>>(list, HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody PostRequestDTO request) {
        PostResponseDTO response = postService.insert(request);
        if (response != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/read/{postID}")
    public ResponseEntity<PostResponseDTO> read(@PathVariable("postID") Integer postID) {

        PostResponseDTO response = postService.findPost(postID);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("update/{postID}")
    public ResponseEntity<Void> update(@PathVariable("postID") Integer postID, @RequestBody PostRequestDTO request) {
        int result = postService.update(postID, request);
        if (result != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("delete/{postID}")
    public ResponseEntity<Void> delete(@PathVariable("postID") Integer postID) {
        int result = postService.delete(postID);
        if (result != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}