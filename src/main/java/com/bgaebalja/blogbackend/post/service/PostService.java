package com.bgaebalja.blogbackend.post.service;

import com.bgaebalja.blogbackend.post.domain.CompletePostRequest;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;

public interface PostService {
    Long createPost(RegisterPostRequest registerPostRequest);

    void completePost(CompletePostRequest completePostRequest);

    Post getPost(Long id);
}
