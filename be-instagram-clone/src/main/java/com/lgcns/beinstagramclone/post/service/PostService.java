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

      
}
