package com.lgcns.beinstagramclone.Image.ctrl;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageRequestDTO;
import com.lgcns.beinstagramclone.post.domain.dto.PostRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageHashtagDTO;
import com.lgcns.beinstagramclone.Image.service.ImageAIService;

@RestController
@RequestMapping("/api/v1/post/ai")
public class ImageCtrl {
    @Autowired
    private ImageAIService imageAIService;

    @PostMapping(value = "/hashtag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageHashtagDTO>> image(
            @ModelAttribute ImageRequestDTO request
    ) {
        List<ImageHashtagDTO> allResults = new ArrayList<>();
        for (MultipartFile image : request.getImages()) {
            List<String> hashtags = imageAIService.analyzeImage(image);

            ImageHashtagDTO dto = new ImageHashtagDTO();
            dto.setHashtags(hashtags);
            allResults.add(dto);
        }

        return ResponseEntity.ok().body(allResults);
    }
}
