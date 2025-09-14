package com.lgcns.beinstagramclone.domain.service;

import com.lgcns.beinstagramclone.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    // 최신 N개
    public List<PostListItemDTO> getLatest(int size) {
        return postRepository.findLatestSummaries(PageRequest.of(0, size)).getContent();
    }

    // 페이징 버전(옵션)
    public Page<PostListItemDTO> getLatestPaged(int page, int size) {
        return postRepository.findLatestSummaries(PageRequest.of(page, size));
    }
}
