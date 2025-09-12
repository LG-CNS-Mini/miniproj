package com.lgncs.inspire_restjpa.openai.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lgncs.inspire_restjpa.openai.domain.dto.ChatResponseDTO;
import com.lgncs.inspire_restjpa.openai.domain.dto.ImageResponseDTO;
import com.lgncs.inspire_restjpa.openai.domain.dto.QuizResponseDTO;
import com.lgncs.inspire_restjpa.openai.service.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v2/inspire/ai")
public class ChatCtrl {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDTO> chat(
            @RequestParam(name = "weather") String weather,
            @RequestParam(name = "location") String location) {
        System.out.println(">>>> chat ctrl path POST ");
        System.out.println(">>>> prompt " + weather);
        System.out.println(">>>> prompt " + location);
        ChatResponseDTO result = chatService.recommendRestaurant(weather, location);
        ////////////////////////
        return ResponseEntity.ok().body(result);

    }

    @PostMapping("/java")
    public ResponseEntity<QuizResponseDTO> quiz() {
        System.out.println(">>>> chat ctrl path POST /quiz");
        QuizResponseDTO result = chatService.generateQuiz();
        ////////////////////////
        return ResponseEntity.ok().body(result);

    }

    @PostMapping("/image")
    public ResponseEntity<ImageResponseDTO> image() {
        System.out.println(">>>> chat ctrl path POST /image");
        ImageResponseDTO result = chatService.imageHashtag();
        ////////////////////////
        return ResponseEntity.ok().body(result);

    }

}
