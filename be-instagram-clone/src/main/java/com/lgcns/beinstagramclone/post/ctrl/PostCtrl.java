package com.lgcns.beinstagramclone.post.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.post.domain.dto.PostRequestDTO;
import com.lgcns.beinstagramclone.post.domain.dto.PostResponseDTO;
import com.lgcns.beinstagramclone.post.service.PostService;

@RestController
@RequestMapping("/api/v1/post")
public class PostCtrl {

    @Autowired
    private PostService postService;

    @GetMapping("/posts/my")
    public ResponseEntity<List<PostResponseDTO>> myPosts(
            @RequestHeader("authorEmail") String email) {
        List<PostResponseDTO> list = postService.select(email);
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> register(
            @ModelAttribute PostRequestDTO request) {
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

    @PutMapping(value = "/update/{postID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> update(
            @PathVariable("postID") Integer postID,
            @RequestPart(value = "content", required = false) String content,
            @RequestPart(value = "hashtags", required = false) List<String> hashtags,
            @RequestPart(value = "authorEmail", required = true) String authorEmail,
            @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages) {

        PostRequestDTO dto = new PostRequestDTO(content, hashtags, authorEmail, postImages);
        PostResponseDTO updated = postService.update(postID, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{postID}")
    public ResponseEntity<Void> delete(@PathVariable("postID") Integer postID) {
        postService.delete(postID);
        return ResponseEntity.noContent().build();
    }
}