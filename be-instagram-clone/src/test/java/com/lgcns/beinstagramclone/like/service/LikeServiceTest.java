package com.lgcns.beinstagramclone.like.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.lgcns.beinstagramclone.like.domain.dto.LikeRequestDTO;
import com.lgcns.beinstagramclone.like.domain.dto.LikeResponseDTO;
import com.lgcns.beinstagramclone.like.domain.entity.LikeEntity;
import com.lgcns.beinstagramclone.like.domain.entity.LikeId;
import com.lgcns.beinstagramclone.like.repository.LikeRepository;

// Mockito를 사용해서 테스트를 진행할 준비를 해주는 어노테이션이야.
@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    
    // @InjectMocks는 가짜 객체(@Mock)들을 이 클래스에 자동으로 주입시켜줘.
    @InjectMocks
    private LikeService likeService;

    // @Mock은 LikeRepository의 가짜 객체를 만들어주는 마법이야! ✨
    @Mock
    private LikeRepository likeRepository;

    // 테스트를 위해 미리 만들어 둔 가짜(더미) 데이터야.
    private final String testEmail = "test@example.com";
    private final int testPostId = 123;
    
    private final LikeRequestDTO likeRequest = LikeRequestDTO.builder()
                                                            .email(testEmail)
                                                            .postId(testPostId)
                                                            .build();

    // "좋아요 상태 및 개수 조회" 기능을 테스트하는 메소드야.
    @Test
    @DisplayName("좋아요 상태 및 개수 조회 테스트")
    void testGetLikeStatus() {
        // given: 좋아요 기록이 있다고 가정하고, 좋아요 개수가 5개라고 가정할게.
        when(likeRepository.existsById(any(LikeId.class))).thenReturn(true);
        when(likeRepository.countByPostId(testPostId)).thenReturn(5);
        
        // when: getLikeStatus() 메소드를 호출해.
        LikeResponseDTO response = likeService.getLikeStatus(testEmail, testPostId);

        // then: 반환된 결과가 예상한 대로 맞는지 확인해.
        assertNotNull(response); // 결과가 null이 아니어야 해.
        assertTrue(response.isLiked()); // 좋아요 상태는 true여야 해.
        assertEquals(5, response.getLikeCount()); // 좋아요 개수는 5여야 해.

        // given: 이번에는 좋아요 기록이 없다고 가정하고, 좋아요 개수가 3개라고 가정할게.
        when(likeRepository.existsById(any(LikeId.class))).thenReturn(false);
        when(likeRepository.countByPostId(testPostId)).thenReturn(3);

        // when: 다시 getLikeStatus() 메소드를 호출해.
        response = likeService.getLikeStatus(testEmail, testPostId);

        // then: 반환된 결과가 예상한 대로 맞는지 다시 확인해.
        assertNotNull(response);
        assertFalse(response.isLiked()); // 좋아요 상태는 false여야 해.
        assertEquals(3, response.getLikeCount()); // 좋아요 개수는 3여야 해.
    }
    
    // "좋아요" 기능을 테스트하는 메소드야.
    @Test
    @DisplayName("좋아요 테스트")
    void testUserLikesPost() {
        // given: 좋아요 개수가 10개였다고 가정할게.
        when(likeRepository.countByPostId(testPostId)).thenReturn(11); // 좋아요 후 개수는 11개가 될 거야.

        // when: userlikesPost() 메소드를 호출해.
        LikeResponseDTO response = likeService.userlikesPost(likeRequest);

        // then: save 메소드가 1번 호출되었는지, 반환된 좋아요 개수가 올바른지 확인해.
        verify(likeRepository, times(1)).save(any(LikeEntity.class));
        assertNotNull(response);
        assertEquals(11, response.getLikeCount());
    }

    // "좋아요 취소" 기능을 테스트하는 메소드야.
    @Test
    @DisplayName("좋아요 취소 테스트")
    void testUserUnlikesPost() {
        // given: 좋아요 개수가 7개였다고 가정할게.
        when(likeRepository.countByPostId(testPostId)).thenReturn(6); // 좋아요 취소 후 개수는 6개가 될 거야.

        // when: userUnlikesPost() 메소드를 호출해.
        likeService.userUnlikesPost(likeRequest);

        // then: deleteById 메소드가 1번 호출되었는지, 반환된 좋아요 개수가 올바른지 확인해.
        verify(likeRepository, times(1)).deleteById(any(LikeId.class));
        assertNotNull(likeService.userUnlikesPost(likeRequest));
        assertEquals(6, likeService.userUnlikesPost(likeRequest).getLikeCount());
    }

}
