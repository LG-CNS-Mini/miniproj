package com.lgcns.beinstagramclone.Image.ctrl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageHashtagDTO;
import com.lgcns.beinstagramclone.Image.service.ImageAIService;

@RestController
@RequestMapping("/api/v1/post/AI")
public class ImageCtrl {
    @Autowired
    private ImageAIService imageAIService;

    @PostMapping(value = "/Hashtag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImageHashtagDTO>> image(@RequestParam(name = "images") List<MultipartFile> images) {

        List<ImageHashtagDTO> allResults = new ArrayList<>();
        for (MultipartFile image : images) {
            List<String> hashtags = imageAIService.analyzeImage(image);

            ImageHashtagDTO dto = new ImageHashtagDTO();
            dto.setHashtags(hashtags);
            allResults.add(dto);
        }

        return ResponseEntity.ok().body(allResults);
    }
}
