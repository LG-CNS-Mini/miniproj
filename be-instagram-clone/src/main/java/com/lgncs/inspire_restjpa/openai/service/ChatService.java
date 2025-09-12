package com.lgncs.inspire_restjpa.openai.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgncs.inspire_restjpa.openai.domain.dto.ImageResponseDTO;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ChatService {

    @Value("${spring.ai.openai.model}")
    private String model;
    @Value("${spring.ai.openai.api.key}")
    private String key;
    @Value("${spring.ai.openai.api.url}")
    private String url;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public ImageResponseDTO imageHashtag(MultipartFile file) {
        System.out.println(">>> service imageHashtag");

        // 이미지 로드
        byte[] imageBytes = null;
        try {
            imageBytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Base64 인코딩
        String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
        String imageDataUrl = "data:image/jpeg;base64," + base64Image;

        // 프롬프트 설정
        String prompt = """
                너는 전문 이미지 분석가야. 아래 이미지를 보고 연관된 해시태그를 5개 JSON 형식으로 추출해줘.
                다른 설명은 하지 말고 아래 형식처럼 JSON만 응답해.
                {
                    "hashtags": ["#예시1", "#예시2", "..."]
                }
                """;

        // 메시지 구성
        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", List.of(
                Map.of("type", "text", "text", prompt),
                Map.of("type", "image_url", "image_url", Map.of("url", imageDataUrl))));

        Map<String, Object> requestMsg = new HashMap<>();
        requestMsg.put("model", model); // gpt-4o 또는 gpt-4-vision-preview
        requestMsg.put("messages", List.of(userMsg));

        String json = null;
        try {
            json = mapper.writeValueAsString(requestMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 요청
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + key)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        String responseJson = null;
        try {
            Response response = client.newCall(request).execute();
            System.out.println(">>>>> response");

            responseJson = response.body().string();
            System.out.println(responseJson);

            JsonNode node = mapper.readTree(responseJson);
            String content = node.at("/choices/0/message/content").asText();

            return mapper.readValue(content, ImageResponseDTO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
