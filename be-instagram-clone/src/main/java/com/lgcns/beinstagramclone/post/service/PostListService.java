package com.lgcns.beinstagramclone.post.service;

import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.dto.SliceResponseDTO;
import com.lgcns.beinstagramclone.post.repository.PostListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostListService {

    @Autowired
    private PostListRepository PostListRepository;

    public SliceResponseDTO<PostListItemDTO> getFollowedFeed(String myEmail, int page, int size, boolean includeMe) {
        var pageable = PageRequest.of(page, size);
        var slice = PostListRepository.findFollowedFeed(myEmail, pageable);
        return new SliceResponseDTO<>(slice.getContent(), page, size, slice.hasNext());
    }
}
