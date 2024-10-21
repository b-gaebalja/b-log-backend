package com.bgaebalja.blogbackend.notification.domain;

import com.bgaebalja.blogbackend.audit.BaseGeneralEntity;
import com.bgaebalja.blogbackend.image.domain.TargetType;
import com.bgaebalja.blogbackend.post.domain.Content;
import com.bgaebalja.blogbackend.user.domain.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Notification extends BaseGeneralEntity {
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Users targetUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Convert(converter = Content.ContentConverter.class)
    @Column(columnDefinition = "TEXT", nullable = false)
    private Content message;

    @Column(name = "check_yn", columnDefinition = "boolean default false")
    private Boolean checkYn;

    @Builder
    private Notification(Users user, TargetType targetType, Long targetId,Content content, Boolean checkYn ){
        targetUser = user;
        this.targetType = targetType;
        this.targetId = targetId;
        message = content;
        this.checkYn = checkYn;
    }

    public static Notification from(Users user, TargetType targetType, Long targetId, Content content, Boolean checkYn){
        return Notification.builder()
                .user(user)
                .targetType(targetType)
                .targetId(targetId)
                .content(content)
                .checkYn(checkYn).build();
    }

    public static Notification substringContent(Content content){
        return Notification.builder()
                .content(content).build();
    }
}
