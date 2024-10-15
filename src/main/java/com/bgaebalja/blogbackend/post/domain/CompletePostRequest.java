package com.bgaebalja.blogbackend.post.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import static com.bgaebalja.blogbackend.post.util.ApiConstant.POST_ID_VALUE;
import static com.bgaebalja.blogbackend.util.ApiConstant.ID_EXAMPLE;

@Getter
public class CompletePostRequest {
    private static final String CONTENT_VALUE = "게시글 내용";
    private static final String CONTENT_EXAMPLE = "첫 번째 게시글입니다.";
    private static final String CONTENT_NO_VALUE_EXCEPTION_MESSAGE = "게시글 내용을 입력하세요.";

    @Schema(description = POST_ID_VALUE, example = ID_EXAMPLE)
    private String id;

    @Schema(description = CONTENT_VALUE, example = CONTENT_EXAMPLE)
    @NotBlank(message = CONTENT_NO_VALUE_EXCEPTION_MESSAGE)
    private String content;
}
