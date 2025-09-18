package com.lgcns.beinstagramclone.image.service;

import com.lgcns.beinstagramclone.Image.service.ImageAIService;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ImageServicedTest2 {

    @Autowired
    private ImageAIService imageAIService;

    @BeforeAll
    static void setUp() {
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();
        env.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    @Test
    void givenOneImageWhenGetHashtagThenReturnFiveHashtags() throws Exception {
        // given
        String testImagePath = "src/test/resources/test-images/도전.png";

        MockMultipartFile image = new MockMultipartFile(
                "image",                  // 파라미터 이름
                "도전.png",               // 원본 파일명
                org.springframework.http.MediaType.IMAGE_PNG_VALUE, // 컨텐츠 타입
                new FileInputStream(testImagePath)
        );

        List<String> result = imageAIService.analyzeImage(image);

        System.out.println(result);

        assertEquals(5, result.size());
    }

}
