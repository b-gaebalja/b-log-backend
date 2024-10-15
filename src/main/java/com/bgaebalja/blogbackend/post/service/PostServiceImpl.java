package com.bgaebalja.blogbackend.post.service;

import com.bgaebalja.blogbackend.post.domain.CompletePostRequest;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;
import com.bgaebalja.blogbackend.post.exception.PostNotFoundException;
import com.bgaebalja.blogbackend.post.repository.PostRepository;
import com.bgaebalja.blogbackend.user.domain.Users;
import com.bgaebalja.blogbackend.user.repository.UserRepository;
import com.bgaebalja.blogbackend.util.FormatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bgaebalja.blogbackend.post.exception.ExceptionMessage.POST_NOT_FOUND_EXCEPTION_MESSAGE;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 20)
    public Long createPost(RegisterPostRequest registerPostRequest) {
        // TODO: 회원 엔티티가 null인 경우 처리
        Users user = userRepository.findByEmailAndDeleteYn(registerPostRequest.getEmail(), false);
        Post post = postRepository.save(Post.from(registerPostRequest, user));

        return post.getId();
    }

    @Override
    public void completePost(CompletePostRequest completePostRequest) {
        Post temporalPost = getTemporalPost(FormatConverter.parseToLong(completePostRequest.getId()));
        temporalPost.complete(completePostRequest.getContent());

        postRepository.save(temporalPost);
    }

    private Post getTemporalPost(Long id) {
        return postRepository.findByIdAndDeleteYnFalseAndCompleteYnFalse(id)
                .orElseThrow(() -> new PostNotFoundException
                        (String.format(POST_NOT_FOUND_EXCEPTION_MESSAGE, id)));
    }
}
