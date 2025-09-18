package com.lgcns.beinstagramclone.post.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.lgcns.beinstagramclone.post.domain.dto.PostListItemDTO;
import com.lgcns.beinstagramclone.post.domain.dto.PostRequestDTO;
import com.lgcns.beinstagramclone.post.domain.dto.PostResponseDTO;
import com.lgcns.beinstagramclone.post.domain.dto.SliceResponseDTO;
import com.lgcns.beinstagramclone.post.service.PostListService;
import com.lgcns.beinstagramclone.post.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post API", description = "게시글 관련 API")
public class PostCtrl {

    @Autowired
    private PostService postService;
    @Autowired
    private PostListService postListService;

    @GetMapping("/following")
        @Operation(
        summary = "팔로잉 피드 조회",
        description = "내가 팔로우하는 사용자의 게시글을 최신순으로 슬라이스 페이징하여 반환합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(schema = @Schema(implementation = SliceResponseDTO.class)))
    public SliceResponseDTO<PostListItemDTO> feedFromFollowing(
            @Parameter(description = "내 이메일", example = "me@example.com")
            @RequestParam String email,
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "5")
            @RequestParam(defaultValue = "5") int size,
            @Parameter(description = "내 글 포함 여부", example = "true")
            @RequestParam(defaultValue = "true") boolean includeMe
    ) {
        return postListService.getFollowedFeed(email, page, size, includeMe);
    }

    @GetMapping("/posts/my")
    @Operation(
        summary = "내 게시글 목록 조회",
        description = "작성자 이메일 기준으로 본인이 작성한 게시글 목록을 반환합니다."
    )
     @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostResponseDTO.class))))
    public ResponseEntity<List<PostResponseDTO>> myPosts(
            @Parameter(description = "작성자 이메일", example = "me@example.com")
            @RequestParam("authorEmail") String email) {
        List<PostResponseDTO> list = postService.select(email);
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "게시글 등록 (multipart/form-data)",
        description = """
            멀티파트로 게시글을 등록합니다.
            - content: 문자열
            - hashtags: 문자열 배열(예: ["#봄날","#산책"])
            - authorEmail: 문자열
            - postImages: 이미지 파일들
            """
    )
    @ApiResponse(responseCode = "201", description = "생성 성공")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    public ResponseEntity<Void> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "게시글 등록 폼",
                content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                schema = @Schema(implementation = PostRequestDTO.class))
            )
            @ModelAttribute PostRequestDTO request) {
        PostResponseDTO response = postService.insert(request);
        if (response != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/read/{postID}")
    @Operation(
        summary = "게시글 단건 조회",
        description = "postID로 게시글 상세를 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공",
        content = @Content(schema = @Schema(implementation = PostResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "게시글 없음",
                    content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<PostResponseDTO> read(
            @Parameter(description = "게시글 ID", example = "11")
            @PathVariable("postID") Integer postID) {

        PostResponseDTO response = postService.findPost(postID);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //@PutMapping(value = "/update/{postID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDTO> update(
            @PathVariable("postID") Integer postID,
            @RequestPart(value = "content", required = false) String content,
            @RequestPart(value = "hashtags", required = false) List<String> hashtags,
            @RequestPart(value = "authorEmail", required = true) String authorEmail,
            @RequestPart(value = "postImages", required = false) List<MultipartFile> postImages) {

        PostRequestDTO dto = new PostRequestDTO(content, hashtags, authorEmail, postImages);
        PostResponseDTO updated = postService.update(postID, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{postID}")
    @Operation(
        summary = "게시글 삭제",
        description = "postID로 게시글을 삭제합니다."
    )
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    public ResponseEntity<Void> delete(
            @Parameter(description = "게시글 ID", example = "11")
            @PathVariable("postID") Integer postID) {
        postService.delete(postID);
        return ResponseEntity.noContent().build();
    }
}