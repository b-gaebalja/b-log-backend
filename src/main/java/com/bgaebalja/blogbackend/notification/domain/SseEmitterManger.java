package com.bgaebalja.blogbackend.notification.domain;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterManger {
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public void addEmitter(Long id, SseEmitter emitter) {
        sseEmitters.put(id, emitter);
        emitter.onCompletion(() -> sseEmitters.remove(id));
        emitter.onTimeout(() -> sseEmitters.remove(id));
        emitter.onError(e -> sseEmitters.remove(id));
    }

    public SseEmitter getEmitter(Long id) {
        return sseEmitters.get(id);
    }

    public void removeEmitter(Long id) {
        sseEmitters.remove(id);
    }

    public void sendEvent(Long targetId, String eventName, String data) {
        SseEmitter sseEmitter = getEmitter(targetId);
        if (sseEmitter == null) {
            return;
        }
        try {
            sseEmitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException e) {
            removeEmitter(targetId); // 오류 발생 시 제거
        }
    }
}
