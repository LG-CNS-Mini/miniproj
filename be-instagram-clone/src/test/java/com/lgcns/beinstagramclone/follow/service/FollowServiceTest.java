package com.lgcns.beinstagramclone.follow.service;

import com.lgcns.beinstagramclone.follow.domain.entity.FollowEntity;
import com.lgcns.beinstagramclone.follow.domain.entity.FollowId;
import com.lgcns.beinstagramclone.follow.repository.FollowRepository;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    private UserEntity follower;
    private UserEntity following;
    private String followerEmail;
    private String followingEmail;
    private FollowId followId;

    @BeforeEach
    void setUp() {
        followerEmail = "follower@test.com";
        followingEmail = "following@test.com";

        follower = UserEntity.builder()
                .email(followerEmail)
                .name("Follower Name")
                .build();

        following = UserEntity.builder()
                .email(followingEmail)
                .name("Following Name")
                .build();

        followId = new FollowId(followerEmail, followingEmail);
    }

    @Test
    @DisplayName("팔로우 성공 테스트")
    void testFollowSuccess() {
        // given: 유저가 존재하고, 아직 팔로우하지 않았다고 가정
        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.of(follower));
        when(userRepository.findByEmail(followingEmail)).thenReturn(Optional.of(following));
        when(followRepository.existsById(followId)).thenReturn(false);

        // when: 팔로우 서비스 호출
        followService.follow(followerEmail, followingEmail);

        // then: 팔로우 객체가 한 번 저장되었는지 검증
        verify(followRepository, times(1)).save(any(FollowEntity.class));
    }

    @Test
    @DisplayName("이미 팔로우한 경우, 예외 발생 테스트")
    void testFollowAlreadyExists() {
        // given: 유저가 존재하고, 이미 팔로우했다고 가정
        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.of(follower));
        when(userRepository.findByEmail(followingEmail)).thenReturn(Optional.of(following));
        when(followRepository.existsById(followId)).thenReturn(true);

        // when & then: IllegalStateException 예외가 발생하는지 검증
        assertThrows(IllegalStateException.class, () -> {
            followService.follow(followerEmail, followingEmail);
        });

        // verify: save 메소드가 호출되지 않았는지 확인
        verify(followRepository, never()).save(any(FollowEntity.class));
    }

    @Test
    @DisplayName("팔로워 유저가 존재하지 않는 경우, 예외 발생 테스트")
    void testFollowerNotFound() {
        // given: 팔로워 유저가 존재하지 않음
        when(userRepository.findByEmail(followerEmail)).thenReturn(Optional.empty());

        // when & then: IllegalArgumentException 예외가 발생하는지 검증
        assertThrows(IllegalArgumentException.class, () -> {
            followService.follow(followerEmail, followingEmail);
        });
    }

    @Test
    @DisplayName("언팔로우 성공 테스트")
    void testUnfollowSuccess() {
        // when: 언팔로우 서비스 호출
        followService.unfollow(followerEmail, followingEmail);

        // then: deleteById 메소드가 한 번 호출되었는지 검증
        verify(followRepository, times(1)).deleteById(followId);
    }
}
