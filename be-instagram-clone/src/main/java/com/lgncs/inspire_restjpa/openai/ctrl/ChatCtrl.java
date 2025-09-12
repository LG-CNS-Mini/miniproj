package com.lgncs.inspire_restjpa.openai.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lgncs.inspire_restjpa.openai.domain.dto.ImageResponseDTO;
import com.lgncs.inspire_restjpa.openai.service.ChatService;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/inspire/ai")
public class ChatCtrl {

    @Autowired
    private ChatService chatService;

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponseDTO> image(@RequestParam(name = "image") MultipartFile image) {
        System.out.println(">>>> chat ctrl path POST /image");
        ImageResponseDTO result = chatService.imageHashtag(image);
        ////////////////////////
        return ResponseEntity.ok().body(result);

    }

}
