package com.lgcns.beinstagramclone.Image.service;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageResponseDTO;
import com.lgcns.beinstagramclone.Image.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;


    public List<ImageResponseDTO> findByPostId(Integer postId) {
        return imageRepository.findByPostId(postId).stream()
                .map(ImageResponseDTO::fromEntity)
                .toList();
    }
}
