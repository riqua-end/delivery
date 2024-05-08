package org.delivery.storeadmin.domain.sse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.storeadmin.domain.authorization.model.UserSession;
import org.delivery.storeadmin.domain.sse.connection.SseConnectionPool;
import org.delivery.storeadmin.domain.sse.connection.model.UserSseConnection;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sse")
public class SseApiController {
    /**
     * SSE 연결 생성 (/api/sse/connect)

     * 클라이언트가 /api/sse/connect 엔드포인트로 연결 요청을 보냅니다.
     * 서버는 UserSseConnection 객체를 생성하고 SseConnectionPool 에 등록합니다.
     * UserSseConnection 은 SseEmitter 를 내부적으로 생성하여 SSE 연결을 준비합니다.

     * 메시지 전송 (/api/sse/push-event)

     * 서버 내부에서 특정 이벤트가 발생하면 (예: 새로운 주문 등)
     * SseConnectionPool 에서 해당 사용자의 UserSseConnection 를 가져옵니다.
     * 해당 객체의 sendMessage 메서드를 호출하여 메시지를 SSE 채널로 발송합니다.
     */

    private final SseConnectionPool sseConnectionPool;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseBodyEmitter connect(
        @Parameter(hidden = true)
        @AuthenticationPrincipal UserSession userSession
    ){
        log.info("login user {}", userSession);

        var userSseConnection = UserSseConnection.connect(
                userSession.getStoreId().toString(),
                sseConnectionPool,
                objectMapper
        );

        sseConnectionPool.addSession(userSseConnection.getUniqueKey(), userSseConnection);

        return userSseConnection.getSseEmitter();
    }

    @GetMapping("/push-event")
    public void pushEvent(
        @Parameter(hidden = true)
        @AuthenticationPrincipal UserSession userSession
    ){
        var userSseConnection = sseConnectionPool.getSession(userSession.getStoreId().toString());

        Optional.ofNullable(userSseConnection)
                .ifPresent(it->{
                    it.sendMessage("hello world");
                });
    }

}
