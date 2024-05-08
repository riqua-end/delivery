package org.delivery.storeadmin.domain.sse.connection.ifs;

import org.delivery.storeadmin.domain.sse.connection.model.UserSseConnection;

public interface ConnectionPoolIfs<T,R> {
    // 연결 풀(SseConnectionPool)을 위한 인터페이스(addSession, getSession, onCompletionCallback 메서드 정의)
    void addSession(T uniqueKey, R session);

    R getSession(T uniqueKey);

    void onCompletionCallback(R session);
}
