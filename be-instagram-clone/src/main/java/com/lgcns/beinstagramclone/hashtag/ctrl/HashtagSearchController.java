package com.lgcns.beinstagramclone.hashtag.ctrl;

import com.lgcns.beinstagramclone.hashtag.service.HashtagSearchService;
import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.dto.SliceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post/hashtags")
public class HashtagSearchController {

    private final HashtagSearchService hashtagSearchService;

    //단일 검색
    @GetMapping("/search")
    public SliceResponseDTO<PostListItemDTO> searchByHashtag(
            @RequestParam("tag") String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return hashtagSearchService.searchByHashtag(tag, page, size);
    }

    //다중 검색
    @GetMapping("/search-many")
    public SliceResponseDTO<PostListItemDTO> searchByAnyHashtags(
            @RequestParam("tags") String tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return hashtagSearchService.searchByAnyHashtags(tags, page, size);//
    }
}
