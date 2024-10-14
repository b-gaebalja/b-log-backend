package com.bgaebalja.blogbackend.post.service;

import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;
import com.bgaebalja.blogbackend.post.repository.PostRepository;
import com.bgaebalja.blogbackend.user.domain.Users;
import com.bgaebalja.blogbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 20)
    public Long createPost(RegisterPostRequest registerPostRequest) {
        Users user = userRepository.findByEmailAndDeleteYn(registerPostRequest.getEmail(),false);
        Post post = postRepository.save(Post.from(registerPostRequest, user));

        return post.getId();
    }
}
