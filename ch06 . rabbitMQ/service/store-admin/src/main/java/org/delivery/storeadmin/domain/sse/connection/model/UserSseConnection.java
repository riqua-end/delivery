package org.delivery.storeadmin.domain.sse.connection.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.delivery.storeadmin.domain.sse.connection.ifs.ConnectionPoolIfs;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Getter
@ToString
@EqualsAndHashCode
public class UserSseConnection {

    /**
     * SseEmitter를 캡슐화하고 SSE 연결에 대한 관리 기능 제공
     * SSE 채널을 초기화하고 연결 종료 및 타임아웃 콜백을 설정
     * 연결 생성 시 연결 풀에 자신을 등록
     * 메시지 발송 메서드(sendMessage) 제공
     */

    private final String uniqueKey;

    private final SseEmitter sseEmitter;

    private final ConnectionPoolIfs<String, UserSseConnection> connectionPoolIfs;

    private final ObjectMapper objectMapper;

    private UserSseConnection(
        String uniqueKey,
        ConnectionPoolIfs<String, UserSseConnection> connectionPoolIfs,
        ObjectMapper objectMapper
    ){
        // key 초기화
        this.uniqueKey = uniqueKey;

        // sse 초기화
        this.sseEmitter = new SseEmitter(60 * 1000L);

        // callback 초기화
        this.connectionPoolIfs = connectionPoolIfs;

        // object mapper 초기화
        this.objectMapper = objectMapper;

        // on completion
        this.sseEmitter.onCompletion(()->{
            // connection pool remove
            this.connectionPoolIfs.onCompletionCallback(this);
        });

        // on timeout
        this.sseEmitter.onTimeout(()->{
            this.sseEmitter.complete();
        });

        // onopen 메세지
        this.sendMessage("onopen", "connect");
    }

    public static UserSseConnection connect(
        String uniqueKey,
        ConnectionPoolIfs<String, UserSseConnection> connectionPoolIfs,
        ObjectMapper objectMapper
    ){
        return new UserSseConnection(uniqueKey, connectionPoolIfs, objectMapper);
    }

    public void sendMessage(String eventName, Object data){

        try{
            var json = this.objectMapper.writeValueAsString(data);
            var event = SseEmitter.event()
                    .name(eventName)
                    .data(json)
                    ;

            this.sseEmitter.send(event);
        }
        catch(IOException e){
            this.sseEmitter.completeWithError(e);
        }
    }

    public void sendMessage(Object data){

        try{
            var json = this.objectMapper.writeValueAsString(data);
            var event = SseEmitter.event()
                    .data(json)
                    ;

            this.sseEmitter.send(event);
        }
        catch(IOException e){
            this.sseEmitter.completeWithError(e);
        }
    }
}
