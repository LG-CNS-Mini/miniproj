package com.lgcns.beinstagramclone.Image.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.beinstagramclone.Image.domain.dto.ImageHashtagDTO;

import okhttp3.*;

@Service
public class ImageAIService {

    @Value("${ai.openai.model}")
    private String model;

    @Value("${ai.openai.api.key}")
    private String key;

    @Value("${ai.openai.api.url}")
    private String url;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<String> analyzeImage(MultipartFile file) {
        try {
            byte[] imageBytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            String imageDataUrl = "data:image/jpeg;base64," + base64Image;

            String prompt = """
                    너는 전문 이미지 분석가야. 아래 이미지를 보고 연관된 해시태그를 5개 JSON 형식으로 추출해줘.
                    JSON 형식의 객체 하나만 응답해. 배열([])을 포함하지 마. 오직 아래 형식으로만 응답해:
                    {
                        "hashtags": ["#예시1", "#예시2", "#예시3", "#예시4", "#예시5"]
                    }
                    그 외 다른 문자는 절대 포함하지 마.
                    """;

            Map<String, Object> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", List.of(
                    Map.of("type", "text", "text", prompt),
                    Map.of("type", "image_url", "image_url", Map.of("url", imageDataUrl))));

            Map<String, Object> requestMsg = new HashMap<>();
            requestMsg.put("model", model);
            requestMsg.put("messages", List.of(userMsg));

            String json = mapper.writeValueAsString(requestMsg);

            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer " + key)
                    .header("Content-Type", "application/json")
                    .post(RequestBody.create(json, MediaType.parse("application/json")))
                    .build();

            Response response = client.newCall(request).execute();
            String responseJson = response.body().string();

            JsonNode node = mapper.readTree(responseJson);
            String content = node.at("/choices/0/message/content").asText();

            ImageHashtagDTO dto = mapper.readValue(content, ImageHashtagDTO.class);
            return dto.getHashtags();

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
