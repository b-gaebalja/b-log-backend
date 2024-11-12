package com.bgaebalja.blogbackend.share.repository;

import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.share.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {
    List<Share> findBySharerId(Long sharerId);
    boolean existsByPostId(Long postId);
    void deleteByPostId(Long postId);
}
