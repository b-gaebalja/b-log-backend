package com.bgaebalja.blogbackend.post.service;

import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;

public interface PostService {
    Long createPost(RegisterPostRequest registerPostRequest);
}
