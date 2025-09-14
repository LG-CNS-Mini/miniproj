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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public List<PostResponseDTO> select() {
        return postRepository.findAllWithImages()
                .stream()
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
                .hashtag(dto.getHashtag())
                .build();
        postRepository.save(post);

        List<MultipartFile> images = dto.getPostImages();

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
        return PostResponseDTO.fromEntity(post);
    }

    @Transactional
    public int update(Integer id, PostRequestDTO post) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        postEntity.setContent(post.getContent());
        return 1;
    }

    @Transactional
    public int delete(Integer id) {
        postRepository.deleteById(id);
        return 1;
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
}