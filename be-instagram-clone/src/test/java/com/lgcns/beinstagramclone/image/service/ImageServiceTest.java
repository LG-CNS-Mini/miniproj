package com.lgcns.beinstagramclone.image.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.Image.domain.dto.ImageResponseDTO;
import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;
import com.lgcns.beinstagramclone.Image.repository.ImageRepository;
import com.lgcns.beinstagramclone.Image.service.ImageService;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    // @InjectMocks는 가짜 객체(@Mock)들을 ImageService에 자동으로 주입시켜줘.
    @InjectMocks
    private ImageService imageService;

    // @Mock은 ImageRepository의 가짜 객체를 만들어주는 마법이야! ✨
    @Mock
    private ImageRepository imageRepository;
    
    // 테스트에 사용할 더미 데이터와 경로를 미리 준비해 둘게.
    private final String testProjectRoot = "src/test/resources";
    private MultipartFile mockFile;
    private ImageEntity dummyImage;

    // 각 테스트 메소드가 실행되기 전에 이 메소드가 먼저 실행돼.
    @BeforeEach
    void setUp() throws Exception {
        // 가짜 MultipartFile 객체를 만들고 동작을 정의해.
        mockFile = org.mockito.Mockito.mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("test_image.jpg");
        // transferTo 메소드는 아무 동작도 하지 않도록 정의했어.
        doNothing().when(mockFile).transferTo(any(File.class));

        // 가짜 ImageEntity 객체를 만들어서 save 메소드가 반환할 값을 정의해.
        dummyImage = ImageEntity.builder()
                                .imageId(1)
                                .imageUrl("/uploads/2025-01-01/test.jpg")
                                .build();
    }

    // 이미지를 성공적으로 업로드하는 케이스를 테스트하는 메소드야.
    @Test
    @DisplayName("이미지 업로드 성공 테스트")
    void testUploadSuccess() throws Exception {
        // given: imageRepository의 save 메소드가 dummyImage를 반환하도록 설정할게.
        when(imageRepository.save(any(ImageEntity.class))).thenReturn(dummyImage);
        
        // when: upload 메소드를 호출해.
        List<ImageResponseDTO> response = imageService.upload(Collections.singletonList(mockFile), testProjectRoot);
        
        // then: 메소드들이 올바르게 호출되었는지, 반환 값이 올바른지 확인해.
        // 1. imageRepository의 save 메소드가 딱 한 번 호출되었는지 확인.
        verify(imageRepository, times(1)).save(any(ImageEntity.class));
        
        // 2. mockFile의 transferTo 메소드가 호출되었는지 확인.
        verify(mockFile, times(1)).transferTo(any(File.class));
        
        // 3. 반환된 리스트가 비어있지 않은지 확인.
        assertNotNull(response);
        assertEquals(1, response.size());
        
        // 4. 반환된 ImageResponseDTO의 값이 예상과 일치하는지 확인.
        ImageResponseDTO resultDto = response.get(0);
        assertEquals(dummyImage.getImageId(), resultDto.getImageId());
        
        // 5. imageUrl이 올바르게 생성되었는지 확인.
        assertNotNull(resultDto.getImageUrl());
    }
}