package com.lgncs.inspire_restjpa.openai.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgncs.inspire_restjpa.openai.domain.dto.ChatResponseDTO;
import com.lgncs.inspire_restjpa.openai.domain.dto.ImageResponseDTO;
import com.lgncs.inspire_restjpa.openai.domain.dto.QuizResponseDTO;

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

    public ChatResponseDTO recommendRestaurant(String weather, String location) {
        System.out.println(">>> service recommendRestaurant");

        // String prompt = "너는 멋진 인공지능이야. 그래서 맛있는 맛집을 추천해줘."+
        // "현재 날씨는 '"+weather+"' 이고 , 위치는 '"+location+"' 이다.\n"+
        // "아래 JSON 형식으로만 응답해줘!!\n"+
        // "{\n"+
        // " \"location\" : \"<지역>\",\n "+
        // " \"weather\" : \"<날씨>\",\n "+
        // " \"restaurants\" : [\n "+
        // " {\"name\" : \"<가게명>\", \"category\" : \"<분류>\", \"reason\" :
        // \"<추천이유>\"}\n"+
        // " ]\n"+
        // "}" ;

        String prompt = """
                    너는 멋진 인공지능이고 맛집 추천 전문가야.
                    맛있는 맛집을 추천해줘.
                    아래 규칙을 반드시 지켜.
                    1. 무조건 JSON 형식으로만 대답해.
                    2. 다른 문장이나 설명 없이 JSON만 출력해.

                    조건:
                    - 날씨: "%s"
                    - 위치: "%s"

                    출력 JSON 예시:
                    {
                        "location": "<지역>",
                        "weather": "<날씨>",
                        "restaurants": [
                            {"name": "<가게명>", "category": "<분류>", "reason": "<추천 이유>"}
                        ]
                    }
                """.formatted(weather, location);

        /*
         * open ai chat api 대화형식(messages) : role , content
         * role : system - ai model , user - prompt
         * { model : gpt-4o-mini
         * messages : [
         * {role : system, content : ""},
         * {role : user, content : prompt},
         * ]
         * }
         */
        // messages 생성
        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "넌 반드시 JSON 포맷을 키질 수 있는 AI Model 이야.");

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);

        Map<String, Object> requestMsg = new HashMap<>();
        requestMsg.put("model", model);
        requestMsg.put("messages", List.of(systemMsg, userMsg));

        // Object -> json 문자열로 변환해서 요청
        String json = null;
        try {
            json = mapper.writeValueAsString(requestMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(">>>> request json ");
        System.out.println(json);

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

            // if(!response.isSuccessful()) {
            // throw new RuntimeException("ERROR : "+response.code()) ;
            // }

            responseJson = response.body().string();
            System.out.println(responseJson);

            JsonNode node = mapper.readTree(responseJson);
            node.at("/choices/0/message/content").asText();
            return mapper.readValue(node.at("/choices/0/message/content").asText(), ChatResponseDTO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ImageResponseDTO imageHashtag() {
        System.out.println(">>> service imageHashtag");

        // 이미지 로드
        String imagePath = "/test_image.jpg"; // resources 폴더 기준
        byte[] imageBytes = null;
        try {
            ClassPathResource imgFile = new ClassPathResource(imagePath);
            imageBytes = imgFile.getInputStream().readAllBytes();
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

    public QuizResponseDTO generateQuiz() {
        System.out.println(">>> service generateQuiz");

        String prompt = """
                    너는 멋진 인공지능이고 국가공인 문제 출제위원이야.
                    자바문제 출제 전공이야.
                    아래 규칙을 반드시 지켜.
                    1. 무조건 JSON 형식으로만 대답해.
                    2. 다른 문장이나 설명 없이 JSON만 출력해.
                    3. 10개의 문제를 만들어야해.
                    4. ` 쓰지마.
                    출력 JSON 예시:
                    {
                    "quizs" : [
                        {
                            "question" : <문제내용>,
                            "options" : "["보기1", "보기2", "보기3", 보기4"]",
                            "answer" : "<정답>"
                        }
                        ]
                    }
                """;

        /*
         * open ai chat api 대화형식(messages) : role , content
         * role : system - ai model , user - prompt
         * { model : gpt-4o-mini
         * messages : [
         * {role : system, content : ""},
         * {role : user, content : prompt},
         * ]
         * }
         */
        // messages 생성
        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "넌 반드시 JSON 포맷을 키질 수 있는 AI Model 이야.");

        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);

        Map<String, Object> requestMsg = new HashMap<>();
        requestMsg.put("model", model);
        requestMsg.put("messages", List.of(systemMsg, userMsg));

        // Object -> json 문자열로 변환해서 요청
        String json = null;
        try {
            json = mapper.writeValueAsString(requestMsg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(">>>> request json ");
        System.out.println(json);

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
            node.at("/choices/0/message/content").asText();
            return mapper.readValue(node.at("/choices/0/message/content").asText(), QuizResponseDTO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
