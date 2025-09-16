package com.lgcns.beinstagramclone.hashtag.service;

import com.lgcns.beinstagramclone.hashtag.repository.HashtagSearchRepository;
import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.dto.SliceResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagSearchService {

    private final HashtagSearchRepository hashtagSearchRepository;

    // 단일 해시태그 검색
    public SliceResponseDTO<PostListItemDTO> searchByHashtag(String rawTag, int page, int size) {
        String tag = normalize(rawTag);
        Pageable pageable = PageRequest.of(page, size);
        Slice<PostListItemDTO> slice = hashtagSearchRepository.findByHashtag(tag, pageable);

        return new SliceResponseDTO<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext()
        );
    }

    // 다중 해시태그(any-match) 검색
    public SliceResponseDTO<PostListItemDTO> searchByAnyHashtags(String raw, int page, int size) {
        List<String> tags = Arrays.stream(raw.split("[,\\s]+"))
                .filter(s -> !s.isBlank())
                .map(this::normalize)
                .collect(Collectors.toList());

        if (tags.isEmpty()) {
            // 비어있는 결과를 직접 생성해서 반환
            return new SliceResponseDTO<>(Collections.emptyList(), page, size, false);
        }

        Pageable pageable = PageRequest.of(page, size);
        Slice<PostListItemDTO> slice = hashtagSearchRepository.findByAnyHashtags(tags, pageable);

        return new SliceResponseDTO<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext()
        );
    }

    private String normalize(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase();
        return t.startsWith("#") ? t.substring(1) : t;
    }
}
