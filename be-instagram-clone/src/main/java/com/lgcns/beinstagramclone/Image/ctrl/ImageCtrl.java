package com.lgcns.beinstagramclone.Image.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.service.ImageAIService;

@RestController
@RequestMapping("/api/v1/post/AI")
public class ImageCtrl {
    @Autowired
    private ImageAIService imageAIService;

    @PostMapping(value = "/Hashtag", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> image(@RequestParam(name = "image") MultipartFile image) {
        System.out.println(">>>> chat ctrl path POST /image");
        List<String> result = imageAIService.analyzeImage(image);

        return ResponseEntity.ok().body(result);

    }
}
