package org.delivery.storeadmin.domain.sse.connection;

import lombok.extern.slf4j.Slf4j;
import org.delivery.storeadmin.domain.sse.connection.ifs.ConnectionPoolIfs;
import org.delivery.storeadmin.domain.sse.connection.model.UserSseConnection;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class SseConnectionPool implements ConnectionPoolIfs<String, UserSseConnection> {
    // ConcurrentHashMap을 이용해 사용자의 고유 식별자(uniqueKey)를 키로, 각 사용자 연결(UserSseConnection)을 값으로 저장
    private static final Map<String, UserSseConnection> connectionPool = new ConcurrentHashMap<>();

    /*  addSession: 새로운 연결 세션 추가
        getSession: 고유 식별자로 세션을 조회
        onCompletionCallback : 연결이 종료되었을 때 처리하는 콜백 함수
    */
    @Override
    public void addSession(String uniqueKey, UserSseConnection userSseConnection) {
        connectionPool.put(uniqueKey, userSseConnection);
    }

    @Override
    public UserSseConnection getSession(String uniqueKey) {
        return connectionPool.get(uniqueKey);
    }

    @Override
    public void onCompletionCallback(UserSseConnection session) {
        log.info("call back connection pool completion : {}", session);
        connectionPool.remove(session.getUniqueKey());
    }
}
