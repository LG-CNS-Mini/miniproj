package com.lgcns.beinstagramclone.post.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;
import com.lgcns.beinstagramclone.Image.repository.ImageRepository;
import com.lgcns.beinstagramclone.comment.domain.dto.CommentResponseDTO;
import com.lgcns.beinstagramclone.comment.domain.entity.CommentEntity;
import com.lgcns.beinstagramclone.comment.repository.CommentRepository;
import com.lgcns.beinstagramclone.comment.service.CommentService;
import com.lgcns.beinstagramclone.hashtag.domain.entity.HashtagEntity;
import com.lgcns.beinstagramclone.hashtag.repository.HashtagRepository;
import com.lgcns.beinstagramclone.like.repository.LikeRepository;
import com.lgcns.beinstagramclone.like.repository.LikeRepository.PostLikeCount;
import com.lgcns.beinstagramclone.post.domain.dto.PostRequestDTO;
import com.lgcns.beinstagramclone.post.domain.dto.PostResponseDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.domain.entity.PostImageEntity;
import com.lgcns.beinstagramclone.post.repository.PostImageRepository;
import com.lgcns.beinstagramclone.post.repository.PostRepository;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.UserRepository;

@Service
public class PostService {
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<PostResponseDTO> select(String email) {
        UserEntity author = userRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저"));

        List<PostEntity> entities = postRepository.findByAuthorOrderByCreateDateDesc(author);
        return entities.stream()
                .map(PostResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public PostResponseDTO insert(PostRequestDTO dto) {
        UserEntity author = userRepository.findById(dto.getAuthorEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        PostEntity post = PostEntity.builder()
                .author(author)
                .content(dto.getContent())
                .build();
        postRepository.save(post);

        List<MultipartFile> images = dto.getPostImages();

        if (dto.getHashtags() != null) {
            dto.getHashtags().stream()
                    .map(this::normalizeTag)
                    .filter(s -> !s.isBlank())
                    .distinct()
                    .forEach(tagName -> {
                        HashtagEntity tag = hashtagRepository.findByName(tagName)
                                .orElseGet(() -> hashtagRepository.save(
                                        HashtagEntity.builder().name(tagName).build()));
                        post.addHashtag(tag);
                    });
        }

        if (images != null && !images.isEmpty()) {
            int order = 1;
            for (MultipartFile file : images) {
                if (file == null || file.isEmpty())
                    continue;
                String imageUrl = saveFileAndReturnPublicUrl(file);
                ImageEntity image = imageRepository.save(ImageEntity.builder()
                        .imageUrl(imageUrl)
                        .build());
                PostImageEntity mapping = PostImageEntity.builder()
                        .post(post)
                        .image(image)
                        .sortOrder(order++)
                        .build();
                post.addImage(mapping);
            }
            postRepository.save(post);
        }
        return PostResponseDTO.fromEntity(post);
    }

@Transactional(readOnly = true)
public PostResponseDTO findPost(Integer postID) {

    PostEntity post = postRepository.findByIdWithImages(postID)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + postID));

    PostResponseDTO dto = PostResponseDTO.fromEntity(post);

    List<CommentResponseDTO> comments = commentService.getTreeByPost(postID);

    long likeCount = likeRepository.countByPostId(postID);

    dto.setComments(comments);
    dto.setLikeCount(likeCount);
    
    return dto;
}

    @Transactional
    public PostResponseDTO update(Integer postID, PostRequestDTO dto) {
        PostEntity post = postRepository.findByIdWithImages(postID)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + postID));

        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }

        if (dto.getHashtags() != null) {
            post.clearHashtags();

            dto.getHashtags().stream()
                    .map(this::normalizeTag)
                    .filter(s -> !s.isBlank())
                    .distinct()
                    .forEach(tagName -> {
                        HashtagEntity tag = hashtagRepository.findByName(tagName)
                                .orElseGet(() -> hashtagRepository.save(HashtagEntity.builder().name(tagName).build()));
                        post.addHashtag(tag);
                    });
        }

        if (dto.getPostImages() != null) {
            List<PostImageEntity> oldMappings = new ArrayList<>(
                    post.getImages() == null ? List.of() : post.getImages());

            post.clearImages();

            for (PostImageEntity m : oldMappings) {
                if (m.getImage() != null) {
                    String url = m.getImage().getImageUrl();
                    try {
                        Path p = resolvePathFromPublicUrl(url);
                        if (p != null)
                            Files.deleteIfExists(p);
                    } catch (IOException ignore) {
                    }
                    imageRepository.delete(m.getImage());
                }
            }

            int order = 1;
            for (MultipartFile file : dto.getPostImages()) {
                if (file == null || file.isEmpty())
                    continue;

                String imageUrl = saveFileAndReturnPublicUrl(file);
                ImageEntity image = imageRepository.save(
                        ImageEntity.builder().imageUrl(imageUrl).build());

                PostImageEntity mapping = PostImageEntity.builder()
                        .post(post)
                        .image(image)
                        .sortOrder(order++)
                        .build();

                post.addImage(mapping);
            }
        }

        return PostResponseDTO.fromEntity(post);
    }

    @Transactional
    public void delete(Integer id) {
        PostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다. id=" + id));

        postRepository.delete(post);
    }

    private String saveFileAndReturnPublicUrl(MultipartFile file) {
        try {
            String original = Optional.ofNullable(file.getOriginalFilename()).orElse("file");
            String ext = original.contains(".") ? original.substring(original.lastIndexOf('.') + 1) : "bin";

            LocalDate today = LocalDate.now();
            Path dir = Paths.get(uploadDir,
                    String.valueOf(today.getYear()),
                    String.format("%02d", today.getMonthValue()),
                    String.format("%02d", today.getDayOfMonth()));

            // 디렉토리 생성 (없으면 자동 생성)
            Files.createDirectories(dir);

            String filename = UUID.randomUUID() + "." + ext;
            Path saved = dir.resolve(filename);
            file.transferTo(saved.toFile());

            // URL 반환 (정적 리소스 핸들러랑 맞춰줘야 함)
            return "/images/" + today.getYear() + "/"
                    + String.format("%02d", today.getMonthValue()) + "/"
                    + String.format("%02d", today.getDayOfMonth()) + "/"
                    + filename;
        } catch (Exception e) {
            throw new RuntimeException("이미지 저장 실패: " + file.getOriginalFilename(), e);
        }
    }

    private Path resolvePathFromPublicUrl(String publicUrl) {
        if (publicUrl == null || publicUrl.isBlank())
            return null;

        String prefix = "/images/";
        if (!publicUrl.startsWith(prefix))
            return null;

        // "2025/09/16/uuid.png"
        String relative = publicUrl.substring(prefix.length());

        // uploadDir = "C:/upload"
        return Paths.get(uploadDir).resolve(relative).toAbsolutePath();
    }

    private String normalizeTag(String s) {
        if (s == null)
            return "";
        s = s.trim();
        if (s.startsWith("#"))
            s = s.substring(1);
        return s.toLowerCase();
    }
}