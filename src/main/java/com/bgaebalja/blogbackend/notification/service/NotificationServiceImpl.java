package com.bgaebalja.blogbackend.notification.service;

import com.bgaebalja.blogbackend.comment.domain.Comment;
import com.bgaebalja.blogbackend.comment.repository.CommentRepository;
import com.bgaebalja.blogbackend.image.domain.TargetType;
import com.bgaebalja.blogbackend.notification.domain.Notification;
import com.bgaebalja.blogbackend.notification.domain.NotificationEventRequest;
import com.bgaebalja.blogbackend.notification.domain.SseEmitterManger;
import com.bgaebalja.blogbackend.notification.repository.NotificationRepository;
import com.bgaebalja.blogbackend.post.domain.Content;
import com.bgaebalja.blogbackend.user.domain.Users;
import com.bgaebalja.blogbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl {
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final SseEmitterManger sseEmitterManger;
    private final UserRepository userRepository;

    @Transactional
    @EventListener
    public void saveNotification (NotificationEventRequest eventRequest){
        Long commentId = eventRequest.getId();

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("일치하는 댓글이 없습니다."));

        Content substringContent = tosubstringContent(comment.getContent().toString()); // 댓글 내용 200자로 자름

        Users targetUser = comment.getParent() != null
                ? comment.getParent().getWriter()
                : comment.getPost().getWriter();


        Long targetId = comment.getParent() != null
                ? comment.getParent().getId()
                : comment.getPost().getId();

        TargetType targetType = comment.getParent() != null
                ? TargetType.COMMENT
                : TargetType.POST;

        Notification notification = Notification.from(targetUser, targetType,
                    targetId, substringContent, false);

        sseEmitterManger.sendEvent(targetUser.getId(), "addComment",
                comment.getWriter().getUsername() + "님이 "+targetType+"에 댓글을 작성하셨습니다." + comment.getContent().toString());

        notificationRepository.save(notification);
    }



    private Content tosubstringContent(String content) {
        if(content.length()>200) {
            content = content.substring(0, 200);
            Content substringContent = (Content.of(content));
            return substringContent;
        }
        return Content.of(content);
    }

}
