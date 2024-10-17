package com.bgaebalja.blogbackend.post.domain;

import com.bgaebalja.blogbackend.audit.BaseGeneralEntity;
import com.bgaebalja.blogbackend.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.bgaebalja.blogbackend.util.EntityConstant.BOOLEAN_DEFAULT_FALSE;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Post extends BaseGeneralEntity {
    @Convert(converter = Content.ContentConverter.class)
    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private Content content;

    @Column(nullable = false, columnDefinition = BOOLEAN_DEFAULT_FALSE)
    private boolean completeYn;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Users writer;

    @Builder
    private Post(Content content, Users user) {
        this.content = content;
        writer = user;
    }

    public static Post from(RegisterPostRequest registerPostRequest, Users user) {
        return Post.builder()
                .content(Content.of(registerPostRequest.getContent()))
                .user(user)
                .build();
    }

    public void complete(String content) {
        this.content = Content.of(content);
        completeYn = true;
    }

    public void update(String content) {
        this.content = Content.of(content);
    }
}
