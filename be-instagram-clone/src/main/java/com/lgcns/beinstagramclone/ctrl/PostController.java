package com.lgcns.beinstagramclone.ctrl;

import com.lgcns.beinstagramclone.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.lgcns.beinstagramclone.domain.dto.SliceResponseDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/following")
    public SliceResponseDTO<PostListItemDTO> feedFromFollowing(
            @RequestParam String email,                 // 내 이메일 (로그인 세션으로 대체 가능)
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "true") boolean includeMe // 내 글 포함 여부
    ) {
        return postService.getFollowedFeed(email, page, size, includeMe);
    }
}