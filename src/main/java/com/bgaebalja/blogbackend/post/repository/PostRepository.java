package com.bgaebalja.blogbackend.post.repository;

import com.bgaebalja.blogbackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
