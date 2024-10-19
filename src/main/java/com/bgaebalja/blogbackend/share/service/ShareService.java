package com.bgaebalja.blogbackend.share.service;

import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.share.domain.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShareService {
    void sharePost(Long postId);
    List<Share> getSharedPosts(Long sharerId);
    void deleteSharedPost(Long shareId);
}

