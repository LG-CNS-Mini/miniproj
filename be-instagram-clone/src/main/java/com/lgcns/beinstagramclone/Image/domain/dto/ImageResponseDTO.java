package com.lgcns.beinstagramclone.Image.domain.dto;

import com.lgcns.beinstagramclone.Image.domain.entity.ImageEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageResponseDTO {

    private Integer imageId;
    private String imageUrl;
    private LocalDateTime createdAt;

    public static ImageResponseDTO fromEntity(ImageEntity entity){
        return ImageResponseDTO.builder()
                .imageId(entity.getImageId())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
