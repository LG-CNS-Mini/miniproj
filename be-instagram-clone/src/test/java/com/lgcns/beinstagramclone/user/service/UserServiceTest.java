package com.lgcns.beinstagramclone.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.repository.ImageRepository;
import com.lgcns.beinstagramclone.user.domain.dto.ProfileImageResponseDTO;
import com.lgcns.beinstagramclone.user.domain.dto.UserProfileResponseDTO;
import com.lgcns.beinstagramclone.user.domain.dto.UserRequestDTO;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.RefreshTokenRepository;
import com.lgcns.beinstagramclone.user.repository.UserRepository;
import com.lgcns.beinstagramclone.util.JwtProvider;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    //#region 1. Fields

    @InjectMocks
    private UserService userService;

    @Autowired
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtProvider jwtProvider;

    //#endregion

    //#region 2. Dummy Data

    private final UserEntity testUser = UserEntity.builder()
        .email("test@example.com")
        .passwd("Lgcns1234")
        .name("Test User")
        .nickname("tester")
        .build();

    private final UserRequestDTO testRequest = UserRequestDTO.builder()
        .email("test@example.com")
        .passwd("Lgcns1234")
        .name("Test User")
        .nickname("tester")
        .build();
    
    //#endregion

    //#region 3. Setup
    @BeforeAll
    static void setUp() {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();
        env.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
    //#endregion

    //#region 4. Test
    @Test
    @DisplayName("회원가입 테스트")
    void testSignup() {
        // given: userRepository.save() 메소드가 호출되면 더미 객체(testUser)를 반환하도록 설정해요.
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        // when: userService의 signup 메소드를 호출해서 테스트를 시작해요.
        userService.signup(testRequest);

        // then: userRepository.save() 메소드가 딱 한 번 호출되었는지 확인해서,
        // 회원가입 로직이 올바르게 동작했는지 검증해요.
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("로그인 테스트")
    void testSignin() {
        // given: Mock 객체들의 동작을 설정합니다.
        when(userRepository.findByEmailAndPasswd(anyString(), anyString())).thenReturn(testUser);
        when(jwtProvider.generateAccessToken(anyString())).thenReturn("mocked_access_token");
        when(jwtProvider.generateRefreshToken(anyString())).thenReturn("mocked_refresh_token");
        when(jwtProvider.getREFRESH_TOKEN_EXPIRY()).thenReturn(1200L); 

        // when: userService의 signin 메소드를 호출합니다.
        userService.signin(testRequest); 

        // then: 필요한 모든 메소드들이 올바르게 호출되었는지 검증합니다.
        verify(userRepository, times(1)).findByEmailAndPasswd(anyString(), anyString());
        verify(jwtProvider, times(1)).generateAccessToken(anyString());
        verify(jwtProvider, times(1)).generateRefreshToken(anyString());
        verify(refreshTokenRepository, times(1)).save(anyString(), anyString(), anyLong());
    }


    @Test
    @DisplayName("로그아웃 테스트")
    void testLogout() {
        String email = "test@example.com";
        userService.logout(email);
        verify(refreshTokenRepository, times(1)).delete(email);
    }

    @Test
    @DisplayName("프로필 이미지 변경 테스트")
    void testUpdateProfileImage() {
        ReflectionTestUtils.setField(userService, "uploadDir", "src/test/resources/static/images");
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("profile.jpg");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        ProfileImageResponseDTO result = userService.updateProfileImage("test@example.com", mockFile);
        
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertNotNull(result);
    }
    
    @Test
    @DisplayName("유저 검색(suggestUsers) 테스트")
    void testSuggestUsers() {
        UserEntity dummyUser = UserEntity.builder()
                .email("test2@example.com")
                .name("Dummy User")
                .nickname("dummy")
                .build();
        when(userRepository.searchByNameOrNickname(anyString(), any()))
            .thenReturn(new PageImpl<>(Collections.singletonList(dummyUser)));

        List<?> result = userService.suggestUsers("dummy", 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).searchByNameOrNickname(anyString(), any());
    }

    @Test
    @DisplayName("유저 프로필 조회(selectUser) 테스트")
    void testSelectUser() {
        UserProfileResponseDTO dummyResponse = UserProfileResponseDTO.builder()
                                                .email("test@example.com")
                                                .name("Test User")
                                                .userImageUrl("http://example.com/image.jpg")
                                                .nickname("tester")
                                                .followerCount(10L)
                                                .followingCount(5L)
                                                .postCount(20L)
                                                .isFollow(1L)
                                                .build();

        when(userRepository.selectUser(anyString(), anyString())).thenReturn(dummyResponse);

        UserProfileResponseDTO result = userService.selectUser("test@example.com", "otheruser@example.com");

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        verify(userRepository, times(1)).selectUser(anyString(), anyString());
    }

    //#endregion

}
