package com.bgaebalja.blogbackend.notification.controller;

import com.bgaebalja.blogbackend.notification.domain.SseEmitterManger;
import com.bgaebalja.blogbackend.user.repository.UserRepository;
import com.bgaebalja.blogbackend.util.FormatValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final SseEmitterManger sseEmitterManger = new SseEmitterManger();
    private final UserRepository userRepository;

    @CrossOrigin
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@RequestParam String email) {
        FormatValidator.validateEmail(email);
        Long id = userRepository.findUsersByEmail(email).getId();

        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try{
            sseEmitter.send(SseEmitter.event().name("connect"));
        }catch(IOException e){
            e.printStackTrace();
        }

        sseEmitterManger.addEmitter(id, sseEmitter);

        return sseEmitter;
    }

}
