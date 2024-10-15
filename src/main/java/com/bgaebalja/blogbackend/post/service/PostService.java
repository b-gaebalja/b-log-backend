package com.bgaebalja.blogbackend.post.service;

import com.bgaebalja.blogbackend.post.domain.CompletePostRequest;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Long createPost(RegisterPostRequest registerPostRequest);

    void completePost(CompletePostRequest completePostRequest);

    Post getPost(Long id);

    Page<Post> getPosts(Pageable pageable, String email);
}
