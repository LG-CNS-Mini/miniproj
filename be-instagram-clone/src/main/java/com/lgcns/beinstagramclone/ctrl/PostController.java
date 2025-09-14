package com.lgcns.beinstagramclone.ctrl;

import com.lgcns.beinstagramclone.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 작성시간 기준 최신 5개 (size 쿼리파라미터로 조절 가능)
    @GetMapping("/latest")
    public List<PostListItemDTO> getLatestFive(@RequestParam(defaultValue = "5") int size) {
        return postService.getLatest(size);
    }
}
