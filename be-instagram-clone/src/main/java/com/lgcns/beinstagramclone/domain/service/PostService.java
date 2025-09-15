package com.lgcns.beinstagramclone.domain.service;

import com.lgcns.beinstagramclone.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lgcns.beinstagramclone.domain.dto.SliceResponseDTO;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public SliceResponseDTO<PostListItemDTO> getFollowedFeed(String myEmail, int page, int size, boolean includeMe) {
        var pageable = PageRequest.of(page, size);
        var slice = includeMe
                ? postRepository.findFollowedFeedIncludingMe(myEmail, pageable)
                : postRepository.findFollowedFeed(myEmail, pageable);
        return new SliceResponseDTO<>(slice.getContent(), page, size, slice.hasNext());
    }
}
