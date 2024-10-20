package com.bgaebalja.blogbackend.share.service;

import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.post.service.PostService;
import com.bgaebalja.blogbackend.share.domain.Share;
import com.bgaebalja.blogbackend.share.repository.ShareRepository;
import com.bgaebalja.blogbackend.post.repository.PostRepository; // PostRepository 추가
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository; // PostRepository 추가

    @Override
    public void sharePost(Long postId, Long sharerId, String url) {
        Post post = postService.getPost(postId);
        Long writerId = post.getWriter().getId(); // 포스트의 작성자 ID
        System.out.println("-------------------"+writerId+"----------------------");

        Share share = new Share(postId, writerId, sharerId, url); // postId로 Share 생성
        shareRepository.save(share);
    }

    @Override
    public List<Share> getSharedPosts(Long sharerId) { //Pageable pageable,
        //return postRepository.findByWriterIdAndDeleteYnFalseAndCompleteYnTrue(sharerId, pageable);
        return shareRepository.findBySharerId(sharerId);
    }

//    @Override
//    public void deleteSharedPost(Long postId) {
//        if (!shareRepository.existsById(postId)) {
//            throw new RuntimeException("Share not found");
//        }
//        shareRepository.deleteByPostId(postId);
//    }

    @Override
    public void deleteSharedPost(Long postId) {
        // 해당 postId와 관련된 Share가 존재하는지 확인
        if (!shareRepository.existsByPostId(postId)) {
            throw new RuntimeException("Share not found for postId: " + postId);
        }
        // 해당 postId와 관련된 Share 삭제
        shareRepository.deleteByPostId(postId);
    }


    // 현재 로그인한 사용자의 ID를 가져오는 메서드
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 여기서 사용자 ID를 가져오는 로직을 변경
            return Long.valueOf(userDetails.getUsername()); // 사용자의 username이 ID라고 가정
        }
        throw new RuntimeException("User not authenticated");
    }
}