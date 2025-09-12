package com.lgcns.beinstagramclone.Image.ctrl;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageResponseDTO;
import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;
import com.lgcns.beinstagramclone.Image.repository.ImageRepository;
import com.lgcns.beinstagramclone.Image.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/files")
public class ImageCtrl {
    @Autowired
    private ImageService imageService;
    private static final String UPLOAD_DIR = "C:/Inspire/miniproj/be-instagram-clone/uploadFiles/";

    @GetMapping("/post/{postId}")
    public List<String> getImagesByPostId(@PathVariable Integer postId) {
        List<ImageResponseDTO> images = imageService.findByPostId(postId);

        // 파일 경로를 API 호출 가능한 URL로 변환
        return images.stream()
                .map(img -> "http://localhost:8088/api/files/" + img.getImageUrl())
                .collect(Collectors.toList());
    }



    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 확장자에 맞춰 변경 가능
                .body(resource);
    }
}
