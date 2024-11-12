package com.bgaebalja.blogbackend.notification.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEventRequest extends ApplicationEvent {
    private final Long id;

    public NotificationEventRequest(Long id) {
        super(id);
        this.id = id;
    }

}
