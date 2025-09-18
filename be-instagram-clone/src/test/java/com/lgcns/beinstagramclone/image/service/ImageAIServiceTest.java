package com.lgcns.beinstagramclone.image.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.beinstagramclone.Image.domain.dto.ImageHashtagDTO;
import com.lgcns.beinstagramclone.Image.service.ImageAIService;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageAIServiceTest {

    @InjectMocks
    private ImageAIService imageAIService;

    @Mock
    private MultipartFile mockFile;
    @Mock
    private OkHttpClient mockClient;
    @Mock
    private Call mockCall;
    @Mock
    private Response mockResponse;
    @Mock
    private ResponseBody mockResponseBody;
    @Mock
    private ObjectMapper mockMapper;

    private final String mockJsonContent = "{\"hashtags\":[\"#test1\", \"#test2\"]}";
    private final String mockApiKey = "test-key";
    private final String mockApiUrl = "http://test.url/api";
    private final String mockModel = "test-model";

    @BeforeEach
    void setUp() {
        // @Value로 주입되는 필드를 ReflectionTestUtils를 사용해서 가짜 값으로 설정해줘.
        ReflectionTestUtils.setField(imageAIService, "key", mockApiKey);
        ReflectionTestUtils.setField(imageAIService, "url", mockApiUrl);
        ReflectionTestUtils.setField(imageAIService, "model", mockModel);

        // ImageAIService의 private final OkHttpClient client와 ObjectMapper mapper도 가짜로 넣어줘.
        ReflectionTestUtils.setField(imageAIService, "client", mockClient);
        ReflectionTestUtils.setField(imageAIService, "mapper", mockMapper);
    }

    @Test
    @DisplayName("이미지 분석 성공 - 해시태그 추출")
    void analyzeImage_Success() throws IOException {
        // given: 성공적인 API 응답을 시뮬레이션할게.
        when(mockFile.getBytes()).thenReturn("test-image-data".getBytes());

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("{\"choices\":[{\"message\":{\"content\":\"" + mockJsonContent + "\"}}]}");

        // mockMapper의 동작을 설정해줘.
        JsonNode mockJsonNode = mock(JsonNode.class);
        JsonNode mockContentNode = mock(JsonNode.class);
        ImageHashtagDTO mockDto = new ImageHashtagDTO();
        mockDto.setHashtags(List.of("#test1", "#test2"));

        when(mockMapper.readTree(anyString())).thenReturn(mockJsonNode);
        when(mockJsonNode.at(anyString())).thenReturn(mockContentNode);
        when(mockContentNode.asText()).thenReturn(mockJsonContent);
        when(mockMapper.readValue(eq(mockJsonContent), eq(ImageHashtagDTO.class))).thenReturn(mockDto);

        // when: 서비스 메소드를 호출해.
        List<String> hashtags = imageAIService.analyzeImage(mockFile);

        // then: 결과가 예상과 일치하는지 검증할게.
        assertNotNull(hashtags);
        assertEquals(2, hashtags.size());
        assertEquals("#test1", hashtags.get(0));
        assertEquals("#test2", hashtags.get(1));

        // verify: 주요 메소드들이 예상대로 호출되었는지 확인할게.
        verify(mockFile, times(1)).getBytes();
        verify(mockClient, times(1)).newCall(any(Request.class));
        verify(mockCall, times(1)).execute();
        verify(mockMapper, times(1)).readTree(anyString());
        verify(mockMapper, times(1)).readValue(eq(mockJsonContent), eq(ImageHashtagDTO.class));
    }

    @Test
    @DisplayName("이미지 분석 실패 - API 통신 오류")
    void analyzeImage_ApiFailure() throws IOException {
        // given: API 호출 시 IOException을 발생시키도록 설정할게.
        when(mockFile.getBytes()).thenReturn("test-image-data".getBytes());
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new IOException());

        // when: 서비스 메소드를 호출해.
        List<String> hashtags = imageAIService.analyzeImage(mockFile);

        // then: 빈 리스트가 반환되는지 검증할게.
        assertNotNull(hashtags);
        assertEquals(0, hashtags.size());
    }

    @Test
    @DisplayName("이미지 분석 실패 - JSON 파싱 오류")
    void analyzeImage_JsonParsingFailure() throws IOException {
        // given: API 응답이 유효하지 않은 JSON이라고 가정할게.
        when(mockFile.getBytes()).thenReturn("test-image-data".getBytes());
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.string()).thenReturn("invalid-json");

        when(mockMapper.readTree(anyString())).thenThrow(new IOException("Invalid JSON"));

        // when: 서비스 메소드를 호출해.
        List<String> hashtags = imageAIService.analyzeImage(mockFile);

        // then: 빈 리스트가 반환되는지 검증할게.
        assertNotNull(hashtags);
        assertEquals(0, hashtags.size());
    }
}
