package com.lgcns.beinstagramclone.post.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.beinstagramclone.post.domain.dto.PostRequestDTO;
import com.lgcns.beinstagramclone.post.domain.dto.PostResponseDTO;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.repository.PostRepository;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.UserRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public List<PostResponseDTO> select() {

        List<PostEntity> entity = postRepository.findAll();
        List<PostResponseDTO> list = new ArrayList<>();
        for (PostEntity postEnity : entity) {
            list.add(PostResponseDTO.fromEntity(postEnity));
        }

        return list;
    }

    @Transactional
    public PostResponseDTO insert(PostRequestDTO request) {
        UserEntity author = userRepository.findById(request.getAuthorEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        PostEntity saved = postRepository.save(request.toEntity(author));
        return PostResponseDTO.fromEntity(saved);
    }

    @Transactional
    public PostResponseDTO findPost(Integer id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        PostResponseDTO post = PostResponseDTO.fromEntity(postEntity);
        return post;
    }

    @Transactional
    public int update(Integer id, PostRequestDTO post) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        postEntity.setContent(post.getContent());
        return 1;
    }

    @Transactional
    public int delete(Integer id){
        postRepository.deleteById(id);
        return 1;
    }

}