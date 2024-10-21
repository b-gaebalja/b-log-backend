package com.bgaebalja.blogbackend.notification.repository;

import com.bgaebalja.blogbackend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Long countAllByTargetUserIdAndCheckYnIsFalse(Long targetUserId);
    List<Notification> findByTargetUserIdAndCheckYnFalse(Long targetUserId);

    List<Notification> findByTargetUserIdOrderByCreatedAtDesc(Long targetUserId);
}
