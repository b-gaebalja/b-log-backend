package com.bgaebalja.blogbackend.post.controller;

import com.bgaebalja.blogbackend.image.domain.Image;
import com.bgaebalja.blogbackend.image.domain.RepresentativeImagesRequest;
import com.bgaebalja.blogbackend.image.service.ImageService;
import com.bgaebalja.blogbackend.post.domain.*;
import com.bgaebalja.blogbackend.post.service.PostService;
import com.bgaebalja.blogbackend.post.util.PageableGenerator;
import com.bgaebalja.blogbackend.util.FormatValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.bgaebalja.blogbackend.post.util.PaginationConstant.*;
import static com.bgaebalja.blogbackend.util.ApiConstant.*;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "게시글 관련 API")
public class PostController {
    private final PostService postService;
    private final ImageService imageService;

    private static final String REGISTER_POST = "게시글 임시 등록";
    private static final String REGISTER_POST_DESCRIPTION
            = "회원 이메일 주소와 게시글 내용을 입력해 게시글을 임시 등록할 수 있습니다.";
    private static final String REGISTER_POST_FORM = "게시글 임시 등록 양식";

    private static final String COMPLETE_POST = "게시글 작성 완료";
    private static final String COMPLETE_POST_DESCRIPTION
            = "게시글 ID와 게시글 내용을 입력해 게시글을 작성 완료할 수 있습니다.";
    private static final String COMPLETE_POST_FORM = "게시글 작성 완료 양식";

    private static final String GET_POST = "게시글 조회";
    private static final String GET_POST_DESCRIPTION = "게시글 ID를 입력해 게시글을 조회할 수 있습니다.";
    private static final String POST_ID = "게시글 ID";

    private static final String GET_ALL_POSTS = "전체 게시글 목록 조회";
    private static final String GET_ALL_POSTS_DESCRIPTION = "전체 게시글 목록을 조회할 수 있습니다." +
            "\n페이징 옵션을 선택할 수 있습니다.";

    private static final String GET_MY_POSTS = "자신의 게시글 목록 조회";
    private static final String GET_MY_POSTS_DESCRIPTION = "자신의 게시글 목록을 조회할 수 있습니다." +
            "\n페이징 옵션을 선택할 수 있습니다.";

    @Operation(summary = REGISTER_POST, description = REGISTER_POST_DESCRIPTION)
    @PostMapping()
    public ResponseEntity<Void> registerPost(
            @Valid @RequestBody @Parameter(description = REGISTER_POST_FORM) RegisterPostRequest registerPostRequest
    ) {
        FormatValidator.validateEmail(registerPostRequest.getEmail());

        return buildResponse(postService.createPost(registerPostRequest));
    }

    private ResponseEntity<Void> buildResponse(Long id) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(id)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(CREATED).headers(headers).build();
    }

    @Operation(summary = COMPLETE_POST, description = COMPLETE_POST_DESCRIPTION)
    @PatchMapping()
    public ResponseEntity<Void> completePost(
            @Valid @RequestBody @Parameter(description = COMPLETE_POST_FORM) CompletePostRequest completePostRequest
    ) {
        FormatValidator.validateId(completePostRequest.getId());
        postService.completePost(completePostRequest);

        return ResponseEntity.status(CREATED).build();
    }

    @Operation(summary = GET_POST, description = GET_POST_DESCRIPTION)
    @GetMapping("/{id}")
    public ResponseEntity<GetPostResponse> getPost
            (@PathVariable("id") @Parameter(description = POST_ID, example = ID_EXAMPLE) String id) {
        FormatValidator.validateId(id);
        Post post = postService.getPost(Long.parseLong(id));

        return ResponseEntity.status(HttpStatus.OK).body(GetPostResponse.from(post));
    }

    @Operation(summary = GET_ALL_POSTS, description = GET_ALL_POSTS_DESCRIPTION)
    @GetMapping()
    public ResponseEntity<GetPostsResponse> getAllPosts
            (@RequestParam(defaultValue = TRUE)
             @Schema(description = IS_PAGINATION_USED, example = TRUE) String paged,
             @RequestParam(defaultValue = ZERO)
             @Schema(description = CURRENT_PAGE_NUMBER, example = ZERO) String pageNumber,
             @RequestParam(defaultValue = TWELVE)
             @Schema(description = NUMBER_OF_ITEMS_PER_PAGE, example = TWELVE) String size,
             @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING)
             @Schema(description = SORTING_METHOD, example = ORDER_BY_CREATED_AT_DESCENDING) String sort) {
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<Post> posts = postService.getPosts(pageable, null);
        List<Image> representativeImages
                = imageService.getRepresentativeImages(RepresentativeImagesRequest.from(posts.getContent()));

        return ResponseEntity.status(HttpStatus.OK).body(GetPostsResponse.from(posts, representativeImages));
    }

    @Operation(summary = GET_MY_POSTS, description = GET_MY_POSTS_DESCRIPTION)
    @GetMapping("/users")
    public ResponseEntity<GetPostsResponse> getMyPosts
            (@RequestParam(name = "email")
             @Schema(description = USER_EMAIL_VALUE, example = EMAIL_EXAMPLE) String email,
             @RequestParam(defaultValue = TRUE)
             @Schema(description = IS_PAGINATION_USED, example = TRUE) String paged,
             @RequestParam(defaultValue = ZERO)
             @Schema(description = CURRENT_PAGE_NUMBER, example = ZERO) String pageNumber,
             @RequestParam(defaultValue = TWELVE)
             @Schema(description = NUMBER_OF_ITEMS_PER_PAGE, example = TWELVE) String size,
             @RequestParam(defaultValue = ORDER_BY_CREATED_AT_DESCENDING)
             @Schema(description = SORTING_METHOD, example = ORDER_BY_CREATED_AT_DESCENDING) String sort) {
        FormatValidator.validateEmail(email);
        Pageable pageable = PageableGenerator.createPageable(paged, pageNumber, size, sort);
        Page<Post> posts = postService.getPosts(pageable, email);
        List<Image> representativeImages
                = imageService.getRepresentativeImages(RepresentativeImagesRequest.from(posts.getContent()));

        return ResponseEntity.status(HttpStatus.OK).body(GetPostsResponse.from(posts, representativeImages));
    }
}
