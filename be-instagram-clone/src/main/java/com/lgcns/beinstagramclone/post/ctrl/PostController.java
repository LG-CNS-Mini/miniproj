package com.lgcns.beinstagramclone.post.ctrl;


import com.lgcns.beinstagramclone.follow.service.FollowService;
import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.service.PostListService;
import com.lgcns.beinstagramclone.post.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.lgcns.beinstagramclone.post.domain.dto.SliceResponseDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostListService postListService;

    @GetMapping("/following")
    public SliceResponseDTO<PostListItemDTO> feedFromFollowing(
            @RequestParam String email,                 // 내 이메일
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean includeMe // 내 글 포함 여부
    ) {
        return postListService.getFollowedFeed(email, page, size, includeMe);
    }
}
