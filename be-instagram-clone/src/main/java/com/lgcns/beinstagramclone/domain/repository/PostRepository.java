package com.lgcns.beinstagramclone.domain.repository;

import com.lgcns.beinstagramclone.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.domain.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(
      value = """
              select new com.lgcns.beinstagramclone.domain.dto.PostListItemDTO(
                  u.name,         
                  p.content,       
                  p.hashtag,       
                  p.createDate     
              )
              from PostEntity p
              join p.author u
              order by p.createDate desc
              """,
      countQuery = "select count(p) from PostEntity p"
    )
    Page<PostListItemDTO> findLatestSummaries(Pageable pageable);
}
