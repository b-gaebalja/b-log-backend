package com.bgaebalja.blogbackend.comment.domain;

import com.bgaebalja.blogbackend.audit.BaseGeneralEntity;
import com.bgaebalja.blogbackend.post.domain.Content;
import com.bgaebalja.blogbackend.post.domain.Post;
import com.bgaebalja.blogbackend.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;
import static jakarta.persistence.FetchType.LAZY;
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseGeneralEntity {
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Users writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Convert(converter = Content.ContentConverter.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private Content content;

    @ManyToOne(fetch = LAZY)
    @Column(name = "parent_id", nullable = true)
    private Comment parent;

    @OneToMany(mappedBy = "parentId", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    private Comment(Users user, Post post, Content content, Comment parent) {
        this.writer = user;
        this.post = post;
        this.content = content;
        this.parent = parent;
    }

    public void modifyContent(RegisterCommentRequest registerCommentRequest) {
        this.content = Content.of(registerCommentRequest.getContent());
    }

    public static Comment from(RegisterCommentRequest registerCommentRequest, Users user, Post post){
        return Comment.builder()
                .content(Content.of(registerCommentRequest.getContent()))
                .user(user)
                .post(post)
                .build();
    }
}
