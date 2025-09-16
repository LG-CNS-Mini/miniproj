package com.lgcns.beinstagramclone.comment.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lgcns.beinstagramclone.comment.domain.dto.CommentRequestDTO;
import com.lgcns.beinstagramclone.comment.domain.dto.CommentResponseDTO;
import com.lgcns.beinstagramclone.comment.domain.entity.CommentEntity;
import com.lgcns.beinstagramclone.comment.repository.CommentRepository;
import com.lgcns.beinstagramclone.post.domain.entity.PostEntity;
import com.lgcns.beinstagramclone.post.repository.PostRepository;
import com.lgcns.beinstagramclone.user.domain.entity.UserEntity;
import com.lgcns.beinstagramclone.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CommentResponseDTO insert(CommentRequestDTO req) {

        Integer parentIdRaw = req.getParentId();
        Integer parentId = (parentIdRaw != null && parentIdRaw == 0) ? null : parentIdRaw;
        PostEntity post = postRepository.findById(req.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post를 찾지 못했습니다. " + req.getPostId()));
        UserEntity author = userRepository.findById(req.getAuthorEmail())
                .orElseThrow(() -> new IllegalArgumentException("User를 찾지 못했습니다. " + req.getAuthorEmail()));

        CommentEntity parent = null;
        int depth = 0;

        if (parentId != null) {
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("상위 댓글을 찾지 못했습니다. " + parentId));

            if (!Objects.equals(parent.getPost().getPostID(), post.getPostID())) {
                throw new IllegalArgumentException("댓글과 대댓글의 Post가 같지 않습니다. ");
            }
            if (parent.getDepth() >= 1)
                throw new IllegalArgumentException("대댓글의 대댓글은 허용되지 않습니다.");

            depth = 1;
        }

        CommentEntity saved = commentRepository.save(
                req.toEntity(author, post, parent, depth));
        return CommentResponseDTO.fromEntity(saved);
    }

    @Transactional
    public void delete(Integer commentId, String requesterEmail) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾지 못했습니다. " + commentId));

        if (!comment.getAuthor().getEmail().equals(requesterEmail)) {
            throw new SecurityException("본인 댓글만 삭제할 수 있습니다. ");
        }
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDTO> getTreeByPost(Integer postId) {
        List<CommentEntity> flat = commentRepository.findAllByPostIdWithAuthor(postId);

        Map<Integer, CommentResponseDTO> dtoById = new LinkedHashMap<>();
        for (CommentEntity e : flat) {
            dtoById.put(e.getCommentId(), CommentResponseDTO.fromEntity(e));
        }

        List<CommentResponseDTO> roots = new ArrayList<>();
        for (CommentResponseDTO dto : dtoById.values()) {
            Integer parentId = dto.getParentId();
            if (parentId == null) {
                roots.add(dto);
            } else {
                CommentResponseDTO parent = dtoById.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(dto);
                } else {
                    roots.add(dto);
                }
            }
        }
        return roots;
    }
}