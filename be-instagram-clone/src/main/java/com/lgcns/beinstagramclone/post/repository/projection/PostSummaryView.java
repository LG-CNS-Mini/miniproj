package com.lgcns.beinstagramclone.post.repository.projection;

import java.time.LocalDateTime;

public interface PostSummaryView {
    Integer getPostId();
    String getAuthorName();
    String getContent();
    LocalDateTime getCreatedAt();
}
