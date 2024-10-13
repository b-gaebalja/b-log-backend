package com.bgaebalja.blogbackend.post.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import static com.bgaebalja.blogbackend.util.ApiConstant.EMAIL_EXAMPLE;
import static com.bgaebalja.blogbackend.util.ApiConstant.USER_EMAIL_VALUE;

@Getter
public class RegisterPostRequest {
    private static final String CONTENT_VALUE = "게시글 내용";
    private static final String CONTENT_EXAMPLE = "첫 번째 게시글입니다.";
    private static final String CONTENT_NO_VALUE_EXCEPTION_MESSAGE = "게시글 내용을 입력하세요.";

    @Schema(description = USER_EMAIL_VALUE, example = EMAIL_EXAMPLE)
    private String email;

    @Schema(description = CONTENT_VALUE, example = CONTENT_EXAMPLE)
    @NotBlank(message = CONTENT_NO_VALUE_EXCEPTION_MESSAGE)
    private String content;
}
