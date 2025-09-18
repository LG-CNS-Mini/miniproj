package com.lgcns.beinstagramclone.hashtag.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;
import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.dto.SliceResponseDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.domain.entity.PostImageEntity;
import com.lgcns.beinstagramclone.hashtag.repository.HashtagSearchRepository;
import com.lgcns.beinstagramclone.post.repository.PostImageRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
public class HashtagSearchServiceTest {

    @InjectMocks
    private HashtagSearchService hashtagSearchService;

    @Mock
    private HashtagSearchRepository hashtagSearchRepository;

    @Mock
    private PostImageRepository postImageRepository;

    private final String testUser = "testUser";
    private final String testContent = "Test Content";
    private final LocalDateTime testDate = LocalDateTime.now();

    // 단일 해시태그 검색 기능을 테스트하는 메소드야.
    @Test
    @DisplayName("단일 해시태그 검색 테스트")
    void testSearchByHashtag() {
        // given: 테스트에 필요한 가짜 데이터를 준비할게.
        String rawTag = "#테스트";
        String normalizedTag = "테스트";
        int page = 0;
        int size = 10;
        
        // PostListItemDTO 객체에 postId가 필요하므로 빌더를 사용해서 생성할게.
        List<PostListItemDTO> postDtos = List.of(PostListItemDTO.builder()
                                                                .postId(1)
                                                                .authorName(testUser)
                                                                .content(testContent)
                                                                .createdAt(testDate)
                                                                .build());
        
        // SliceImpl을 사용하여 가짜 Slice 객체를 만들 거야.
        Slice<PostListItemDTO> slice = new SliceImpl<>(postDtos, PageRequest.of(page, size), false);
        
        // PostImageEntity를 사용하여 가짜 이미지 데이터를 준비할게.
        PostImageEntity postImage = PostImageEntity.builder()
                                                   .post(PostEntity.builder().postID(1).build())
                                                   .image(ImageEntity.builder().imageUrl("http://example.com/image.jpg").build())
                                                   .build();
                                                   
        List<PostImageEntity> images = Collections.singletonList(postImage);

        // when: Mock 객체들의 동작을 정의할게.
        when(hashtagSearchRepository.findByHashtag(eq(normalizedTag), any(Pageable.class))).thenReturn(slice);
        when(postImageRepository.findByPost_PostIDIn(anyCollection())).thenReturn(images);

        // when: 서비스 메소드를 호출해.
        SliceResponseDTO<PostListItemDTO> response = hashtagSearchService.searchByHashtag(rawTag, page, size);

        // then: 결과가 예상과 일치하는지 검증할게.
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("http://example.com/image.jpg", response.getItems().get(0).getImageUrls().get(0));
        assertFalse(response.isHasNext());
        
        // verify: 메소드들이 예상대로 호출되었는지 확인할게.
        verify(hashtagSearchRepository, times(1)).findByHashtag(eq(normalizedTag), any(Pageable.class));
        verify(postImageRepository, times(1)).findByPost_PostIDIn(anyCollection());
    }

    // 다중 해시태그(any-match) 검색 기능을 테스트하는 메소드야.
    @Test
    @DisplayName("다중 해시태그 검색 테스트")
    void testSearchByAnyHashtags() {
        // given: 가짜 데이터를 준비할게.
        String rawTags = " #테스트, #개발자 ";
        List<String> normalizedTags = List.of("테스트", "개발자");
        int page = 0;
        int size = 10;

        // PostListItemDTO 객체에 postId가 필요하므로 빌더를 사용해서 생성할게.
        List<PostListItemDTO> postDtos = List.of(PostListItemDTO.builder()
                                                                .postId(1)
                                                                .authorName(testUser)
                                                                .content(testContent)
                                                                .createdAt(testDate)
                                                                .build());
        Slice<PostListItemDTO> slice = new SliceImpl<>(postDtos, PageRequest.of(page, size), false);
        
        PostImageEntity postImage = PostImageEntity.builder()
                                                   .post(PostEntity.builder().postID(1).build())
                                                   .image(ImageEntity.builder().imageUrl("http://example.com/image.jpg").build())
                                                   .build();
        List<PostImageEntity> images = Collections.singletonList(postImage);

        // when: Mock 객체들의 동작을 정의할게.
        when(hashtagSearchRepository.findByAnyHashtags(eq(normalizedTags), any(Pageable.class))).thenReturn(slice);
        when(postImageRepository.findByPost_PostIDIn(anyCollection())).thenReturn(images);

        // when: 서비스 메소드를 호출해.
        SliceResponseDTO<PostListItemDTO> response = hashtagSearchService.searchByAnyHashtags(rawTags, page, size);

        // then: 결과가 예상과 일치하는지 검증할게.
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("http://example.com/image.jpg", response.getItems().get(0).getImageUrls().get(0));
        assertFalse(response.isHasNext());

        // verify: 메소드들이 예상대로 호출되었는지 확인할게.
        verify(hashtagSearchRepository, times(1)).findByAnyHashtags(eq(normalizedTags), any(Pageable.class));
        verify(postImageRepository, times(1)).findByPost_PostIDIn(anyCollection());
    }
    
    // 비어있는 태그로 다중 검색할 때의 기능을 테스트하는 메소드야.
    @Test
    @DisplayName("다중 해시태그 검색 - 빈 태그 입력 시 빈 결과 반환 테스트")
    void testSearchByAnyHashtagsWithEmptyInput() {
        // given: 비어있는 문자열을 태그로 사용할게.
        String rawTags = " ";
        int page = 0;
        int size = 10;
        
        // when: 서비스 메소드를 호출해.
        SliceResponseDTO<PostListItemDTO> response = hashtagSearchService.searchByAnyHashtags(rawTags, page, size);
        
        // then: 결과가 예상과 일치하는지 검증할게.
        assertNotNull(response);
        assertEquals(0, response.getItems().size());
        assertFalse(response.isHasNext());
        
        // verify: Repository 메소드가 호출되지 않았는지 확인할게.
        verify(hashtagSearchRepository, times(0)).findByAnyHashtags(any(), any());
        verify(postImageRepository, times(0)).findByPost_PostIDIn(any());
    }
}
