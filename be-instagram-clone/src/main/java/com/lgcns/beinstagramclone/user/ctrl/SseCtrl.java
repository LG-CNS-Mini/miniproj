package com.lgcns.beinstagramclone.user.ctrl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lgcns.beinstagramclone.user.domain.dto.SseMessageResponseDTO;

@RestController
@RequestMapping("/api/v2/inspire/sse")
public class SseCtrl {

    // 사용자별 SSE 구독 저장
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> userEmitters 
        = new ConcurrentHashMap<>();
    
    // 사용자별 알림 히스토리 문자열
    // private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> userNotifications 
    //     = new ConcurrentHashMap<>();

    // 객체
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseMessageResponseDTO>> userNotifications 
        = new ConcurrentHashMap<>();
    
    // 클라이언트가 SSE 구독할 수 있도록 준비
    @GetMapping(value = "/subscribe/{email}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("email") String email) {
        System.out.println(">>> sse ctrl GET /subscribe ");
        System.out.println(">>> sse ctrl param : "+email); 
        
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.computeIfAbsent(email, k -> new CopyOnWriteArrayList<>()).add(emitter);


        emitter.onCompletion(() -> userEmitters.remove(email));
        emitter.onTimeout(()    -> userEmitters.remove(email));
        emitter.onError((e)     -> userEmitters.get(email).remove(emitter));
        
        // 구독 즉시 연결 확인용
        try {
            emitter.send(SseEmitter.event().name("connected").data("SSE 연결 성공"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    // 로그인 시 SSE 메시지 생성과 알림푸시(문자열기반) 
    // public void sendLoginPush(String email, String message) {
    //     System.out.println(">>> sse ctrl sendLoginPush ");
    //     System.out.println(">>> sse ctrl sendLoginPush " + email + " / " + message);
        
    //     // 히스토리에 저장
    //     userNotifications.computeIfAbsent(email, k -> new CopyOnWriteArrayList<>()).add(message);
    //     // 구독자에게 푸시
    //     List<SseEmitter> emitters = userEmitters.get(email);
    //     if (emitters != null) {
    //         emitters.forEach(emitter -> {
    //             try {
    //                 emitter.send(SseEmitter.event().name("login").data(message));
    //             } catch (IOException e) {
    //                 emitter.completeWithError(e);
    //                 emitters.remove(emitter);
    //             }
    //         });
    //     }
    // }

    // 객체
    public void sendLoginPush(String email, SseMessageResponseDTO message) {
        System.out.println(">>> sse ctrl sendLoginPush ");
        System.out.println(">>> sse ctrl sendLoginPush " + email + " / " + message);
        
        // 히스토리에 저장 (문자열로 저장하려면 JSON 직렬화)
        userNotifications.computeIfAbsent(email, k -> new CopyOnWriteArrayList<>())
                        .add(message);

        // 구독자에게 푸시
        List<SseEmitter> emitters = userEmitters.get(email);
        ObjectMapper mapper = new ObjectMapper();
        if (emitters != null) {
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                                            .name("login")
                                            .data(mapper.writeValueAsString(message))); // 객체 전송
                } catch (IOException e) {
                    emitter.completeWithError(e);
                    emitters.remove(emitter);
                }
            });
        }
    }

    // 문자열
    // @GetMapping("/notifications/{email}")
    // public List<String> getNotifications(@PathVariable String email) {
    //     System.out.println(">>> sse ctrl GET /notifications  ");
    //     return userNotifications.getOrDefault(email, 
    //         new CopyOnWriteArrayList<>());
    // }

    // 객체
    @GetMapping("/notifications/{email}")
    public List<SseMessageResponseDTO> getNotifications(@PathVariable String email) {
        System.out.println(">>> sse ctrl GET /notifications");
        return userNotifications.getOrDefault(email, new CopyOnWriteArrayList<>());
    }

    
}
