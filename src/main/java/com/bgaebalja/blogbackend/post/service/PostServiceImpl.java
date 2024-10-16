package com.bgaebalja.blogbackend.post.service;

import com.bgaebalja.blogbackend.exception.UserNotFoundException;
import com.bgaebalja.blogbackend.post.domain.CompletePostRequest;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.domain.RegisterPostRequest;
import com.bgaebalja.blogbackend.post.exception.PostNotFoundException;
import com.bgaebalja.blogbackend.post.repository.PostRepository;
import com.bgaebalja.blogbackend.user.domain.Users;
import com.bgaebalja.blogbackend.user.repository.UserRepository;
import com.bgaebalja.blogbackend.util.FormatConverter;
import com.bgaebalja.blogbackend.util.FormatValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bgaebalja.blogbackend.exception.ExceptionMessage.USER_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.bgaebalja.blogbackend.post.exception.ExceptionMessage.POST_NOT_FOUND_EXCEPTION_MESSAGE;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;
import static org.springframework.transaction.annotation.Isolation.READ_UNCOMMITTED;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 20)
    public Long createPost(RegisterPostRequest registerPostRequest) {
        String writerEmail = registerPostRequest.getEmail();
        Users writer = userRepository.findByEmailAndDeleteYn(writerEmail, false);
        if (!FormatValidator.hasValue(writer)) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_EXCEPTION_MESSAGE, writerEmail));
        }
        Post post = postRepository.save(Post.from(registerPostRequest, writer));

        return post.getId();
    }

    @Override
    @Transactional(isolation = READ_COMMITTED, timeout = 20)
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

    @Override
    @Transactional(isolation = READ_UNCOMMITTED, readOnly = true, timeout = 10)
    public Post getPost(Long id) {
        return postRepository.findByIdAndDeleteYnFalseAndCompleteYnTrue(id)
                .orElseThrow(() -> new PostNotFoundException
                        (String.format(POST_NOT_FOUND_EXCEPTION_MESSAGE, id)));
    }

    @Override
    @Transactional(isolation = READ_UNCOMMITTED, readOnly = true, timeout = 30)
    public Page<Post> getPosts(Pageable pageable, String email) {
        if (!FormatValidator.hasValue(email)) {
            return postRepository.findByDeleteYnFalseAndCompleteYnTrue(pageable);
        }

        Users user = userRepository.findByEmailAndDeleteYn(email, false);
        if (!FormatValidator.hasValue(user)) {
            throw new UserNotFoundException(String.format(USER_NOT_FOUND_EXCEPTION_MESSAGE, email));
        }

        return postRepository.findByWriterIdAndDeleteYnFalseAndCompleteYnTrue(user.getId(), pageable);
    }
}
