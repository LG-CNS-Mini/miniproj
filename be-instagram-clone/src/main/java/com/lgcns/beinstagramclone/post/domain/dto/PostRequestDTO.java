package com.lgcns.beinstagramclone.post.domain.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostRequestDTO {
    private String content;
    private List<String> hashtags = new ArrayList<>();

    private String authorEmail;

    private List<MultipartFile> postImages = new ArrayList<>();

    public PostEntity toEntity(UserEntity author) {
        return PostEntity.builder()
                .content(this.content)
                .author(author)
                .build();
    }
}
