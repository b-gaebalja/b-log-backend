package com.bgaebalja.blogbackend.comment.controller;

import com.bgaebalja.blogbackend.comment.domain.Comment;
import com.bgaebalja.blogbackend.comment.domain.RegisterCommentRequest;
import com.bgaebalja.blogbackend.comment.service.CommentService;
import com.bgaebalja.blogbackend.util.FormatValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.bgaebalja.blogbackend.util.CommentConstant.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "댓글 관련 API")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = LOAD_COMMENTS, description = LOAD_COMMENTS_DESCRIPTION)
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<Comment>> getComment(@PathVariable Long postId) {
        List<Comment> commentList = commentService.getComments(postId);
        return new ResponseEntity<> (commentList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Comment>> toComment(@PathVariable Long id) {
        List<Comment> commentList = commentService.getComments(id);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @Operation(summary = REGISTER_COMMENT, description = REGISTER_COMMENT_DESCRIPTION)
    @PostMapping("/")
    public ResponseEntity<Void> registerComment(@Valid @RequestBody RegisterCommentRequest registerCommentRequest)
    {
        FormatValidator.validateEmail(registerCommentRequest.getEmail());
        Long id = commentService.createComment(registerCommentRequest);

        return buildResponse(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> modifyComment(@PathVariable Long id,
                                              @Valid @RequestBody RegisterCommentRequest registerCommentRequest){
        FormatValidator.validateEmail(registerCommentRequest.getEmail());
        commentService.modifyComment(registerCommentRequest,id);

        return buildResponse(id);
    }

    @PatchMapping("/{id}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        Long postId = commentService.deleteComment(id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(postId)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }


    private ResponseEntity<Void> buildResponse(Long id) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(id)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }
}
