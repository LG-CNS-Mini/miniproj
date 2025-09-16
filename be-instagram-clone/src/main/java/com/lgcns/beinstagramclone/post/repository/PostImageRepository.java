package com.lgcns.beinstagramclone.post.repository;

import com.lgcns.beinstagramclone.post.domain.entity.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImageEntity, Integer> {

    // 글 id 목록에 해당하는 이미지 전부 한 번에
    List<PostImageEntity> findByPost_PostIDIn(Collection<Integer> postIds);

}
