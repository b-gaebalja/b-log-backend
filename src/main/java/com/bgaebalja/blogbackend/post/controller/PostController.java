package com.bgaebalja.blogbackend.post.controller;

import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;
import com.bgaebalja.blogbackend.post.service.PostService;
import com.bgaebalja.blogbackend.util.FormatValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Post Controller", description = "게시글 관련 API")
public class PostController {
    private final PostService postService;

    private static final String REGISTER_POST = "게시글 등록";
    private static final String REGISTER_POST_DESCRIPTION = "회원 ID와 게시글 내용을 입력해 게시글을 등록할 수 있습니다.";
    private static final String REGISTER_POST_FORM = "게시글 등록 양식";

    @Operation(summary = REGISTER_POST, description = REGISTER_POST_DESCRIPTION)
    @PostMapping()
    public ResponseEntity<Void> registerPost(
            @Valid @RequestBody @Parameter(description = REGISTER_POST_FORM) RegisterPostRequest registerPostRequest
    ) {
        FormatValidator.validateEmail(registerPostRequest.getEmail());
        Long id = postService.createPost(registerPostRequest);

        return buildResponse(id);
    }

    private ResponseEntity<Void> buildResponse(Long id) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(id)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }
}
