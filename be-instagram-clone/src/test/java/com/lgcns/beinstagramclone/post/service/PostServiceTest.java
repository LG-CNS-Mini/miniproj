package com.lgcns.beinstagramclone.post.service;

import com.lgcns.beinstagramclone.Image.repository.ImageRepository;
import com.lgcns.beinstagramclone.comment.repository.CommentRepository;
import com.lgcns.beinstagramclone.comment.service.CommentService;
import com.lgcns.beinstagramclone.hashtag.repository.HashtagRepository;
import com.lgcns.beinstagramclone.like.repository.LikeRepository;
import com.lgcns.beinstagramclone.post.domain.dto.PostResponseDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.repository.PostRepository;
import com.lgcns.beinstagramclone.user.domain.dto.UserProfileResponseDTO;
import com.lgcns.beinstagramclone.user.domain.dto.UserRequestDTO;
import com.lgcns.beinstagramclone.user.domain.dto.UserResponseDTO;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.UserRepository;
import com.lgcns.beinstagramclone.user.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;

    @BeforeAll
    static void setUp() {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();
        env.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentService commentService;

    @Autowired
    UserService userService;
    @Test
    @Transactional
    void givenUserAndOnePostInsertWhenSelectThenReturnOnePost() {
        // given
        String userEmail = saveUser();
        PostEntity result = postRepository.save(
                PostEntity.builder()
                        .author(userRepository.findById(userEmail).get())
                        .content("content")
                        .build()
        );
        
        // when
        Optional<PostEntity> foundPost = postRepository.findById(result.getPostID());
        
        // then
        assertTrue(foundPost.isPresent());
        assertEquals("content", foundPost.get().getContent());
        assertEquals(userEmail, foundPost.get().getAuthor().getEmail());

        // cleanup
        try {
            postRepository.deleteById(result.getPostID());
            Optional<PostEntity> deletedPost = postRepository.findById(result.getPostID());
            assertTrue(deletedPost.isEmpty(), "게시글이 정상적으로 삭제되지 않았습니다.");
        } catch (Exception e) {
            fail("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    @Test
    @Transactional
    void givenPostWhenSavePostThenFindOnePost() {
        // given
        String userEmail = saveUser();
        UserEntity author = userRepository.findById(userEmail).get();
        PostEntity post = PostEntity.builder()
                .author(author)
                .content("테스트 게시글입니다.")
                .build();

        // when
        PostEntity savedPost = postRepository.save(post);
        PostEntity foundPost = postRepository.findById(savedPost.getPostID())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // then
        assertNotNull(foundPost);
        assertEquals("테스트 게시글입니다.", foundPost.getContent());
        assertEquals(author.getEmail(), foundPost.getAuthor().getEmail());
        assertEquals(author.getName(), foundPost.getAuthor().getName());

        // cleanup
        try {
            postRepository.deleteById(savedPost.getPostID());
            Optional<PostEntity> deletedPost = postRepository.findById(savedPost.getPostID());
            assertTrue(deletedPost.isEmpty(), "게시글이 정상적으로 삭제되지 않았습니다.");
        } catch (Exception e) {
            fail("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    @Test
    @Transactional
    void givenSaveOnePostWhenFindPostThenReturnOnePost() {
        // given
        String userEmail = saveUser();
        UserEntity author = userRepository.findById(userEmail).get();
        String testContent = "테스트 게시글 내용입니다.";
        
        PostEntity newPost = PostEntity.builder()
                .author(author)
                .content(testContent)
                .build();
        
        PostEntity savedPost = postRepository.save(newPost);
        
        // when
        PostResponseDTO foundPost = postService.findPost(savedPost.getPostID());
        
        // then
        assertNotNull(foundPost, "저장된 게시글을 찾을 수 없습니다.");
        assertEquals(testContent, foundPost.getContent(), "게시글 내용이 일치하지 않습니다.");
        assertEquals(author.getEmail(), foundPost.getAuthorEmail(), "작성자 이메일이 일치하지 않습니다.");
        assertNotNull(foundPost.getCreateDate(), "생성 날짜가 없습니다.");
        
        // cleanup
        try {
            postRepository.deleteById(savedPost.getPostID());
            Optional<PostEntity> deletedPost = postRepository.findById(savedPost.getPostID());
            assertTrue(deletedPost.isEmpty(), "게시글이 정상적으로 삭제되지 않았습니다.");
        } catch (Exception e) {
            fail("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    @Test
    @Transactional
    void givenSavedPostWhenDeleteThenPostShouldBeDeleted() {
        // given
        String userEmail = saveUser();
        UserEntity author = userRepository.findById(userEmail).get();
        PostEntity post = PostEntity.builder()
                .author(author)
                .content("삭제될 테스트 게시글입니다.")
                .build();

        PostEntity savedPost = postRepository.save(post);
        Integer postId = savedPost.getPostID();

        // 게시글이 정상적으로 저장되었는지 확인
        assertTrue(postRepository.findById(postId).isPresent(), "게시글이 저장되지 않았습니다.");

        // when
        postService.delete(postId);

        // then
        Optional<PostEntity> deletedPost = postRepository.findById(postId);
        assertTrue(deletedPost.isEmpty(), "게시글이 정상적으로 삭제되지 않았습니다.");

        // 추가 검증: 존재하지 않는 게시글 삭제 시도
        Integer nonExistentPostId = 99999;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.delete(nonExistentPostId);
        });

        String expectedMessage = "존재하지 않는 게시글입니다. id=" + nonExistentPostId;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "잘못된 예외 메시지가 출력되었습니다.");
    }

    private String saveUser(){
        String userEmail = "jins7251@gmail.com";
        String passwd = "Vcc12345";
        Optional<UserEntity> response = userRepository.findByEmail(userEmail);

        if(response.isPresent()) {
            return userEmail;
        }

        UserResponseDTO responseDTO = userService.signup(
                UserRequestDTO.builder()
                        .email(userEmail)
                        .passwd(passwd)
                        .nickname("현수 GMAIL")
                        .name("진현수")
                        .build()
        );
        return responseDTO.getEmail();
    }


}