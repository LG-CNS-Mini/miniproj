package com.lgcns.beinstagramclone.post.service;

import com.lgcns.beinstagramclone.like.repository.LikeRepository;
import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.dto.SliceResponseDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostImageEntity;
import com.lgcns.beinstagramclone.post.repository.PostListRepository;
import com.lgcns.beinstagramclone.post.repository.PostImageRepository;
import com.lgcns.beinstagramclone.post.repository.projection.PostSummaryView;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostListService {

    private final PostListRepository postListRepository;
    private final PostImageRepository postImageRepository;
    private final LikeRepository likeRepository;

    public SliceResponseDTO<PostListItemDTO> getFollowedFeed(String myEmail, int page, int size, boolean includeMe) {
        var pageable = PageRequest.of(page, size);
        // 글 목록조회
        var slice = postListRepository.findFollowedFeedSummary(myEmail, pageable);

        var summaries = slice.getContent();
        if (summaries.isEmpty()) {
            return new SliceResponseDTO<>(List.of(), page, size, false);
        }

        List<Integer> postIds = summaries.stream()
                .map(PostSummaryView::getPostId)
                .filter(Objects::nonNull)
                .toList();

        // 이미지, 좋아요개수, 내가좋아요한글
        List<PostImageEntity> images = postImageRepository.findByPost_PostIDIn(postIds);
        Map<Integer, List<String>> imagesByPostId = images.stream()
                .sorted(Comparator.comparing(PostImageEntity::getSortOrder, Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.groupingBy(
                        img -> img.getPost().getPostID(),
                        Collectors.mapping(pi -> pi.getImage().getImageUrl(), Collectors.toList())
                ));

       // 좋아요개수
        Map<Integer, Long> likeCountMap = likeRepository.countByPostIds(postIds).stream()
                .collect(Collectors.toMap(
                        LikeRepository.PostLikeCount::getPostId,
                        LikeRepository.PostLikeCount::getCnt
                ));

        // 내가 좋아요한 글
        Set<Integer> myLikedPostIds = new HashSet<>(likeRepository.findMyLikedPostIds(postIds, myEmail));

        // DTO 변환
        var items = summaries.stream()
                .map(s -> PostListItemDTO.builder()
                        .postId(s.getPostId())
                        .authorName(s.getAuthorName())
                        .content(s.getContent())
                        .createdAt(s.getCreatedAt())
                        .imageUrls(imagesByPostId.getOrDefault(s.getPostId(), List.of()))
                        .likeCount(likeCountMap.getOrDefault(s.getPostId(), 0L))
                        .likedByMe(myLikedPostIds.contains(s.getPostId()))
                        .build()
                )
                .toList();

        return new SliceResponseDTO<>(items, slice.getNumber(), slice.getSize(), slice.hasNext());
    }
}
