package com.lgcns.beinstagramclone.Image.service;

import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageResponseDTO;
import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;
import com.lgcns.beinstagramclone.Image.repository.ImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepository;

    public List<ImageResponseDTO> upload(List<MultipartFile> files, String projectRoot) throws Exception {
        String day = java.time.LocalDate.now().toString(); // 2025-09-11
        Path dir = java.nio.file.Paths.get(projectRoot, "uploads", day);
        java.nio.file.Files.createDirectories(dir);

        List<ImageResponseDTO> out = new java.util.ArrayList<>();
        for (MultipartFile f : files) {
            String ext = java.util.Optional.ofNullable(f.getOriginalFilename())
                    .filter(n -> n.contains("."))
                    .map(n -> n.substring(n.lastIndexOf('.')))
                    .orElse(".bin");
            String name = java.util.UUID.randomUUID() + ext;
            java.nio.file.Path save = dir.resolve(name);
            f.transferTo(save.toFile());

            String url = "/uploads/" + day + "/" + name;
            ImageEntity saved = imageRepository.save(ImageEntity.builder().imageUrl(url).build());
            out.add(new ImageResponseDTO(saved.getImageId(), saved.getImageUrl()));
        }
        return out;
    }
}