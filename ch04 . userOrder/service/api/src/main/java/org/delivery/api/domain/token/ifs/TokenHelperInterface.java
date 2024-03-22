package org.delivery.api.domain.token.ifs;

import org.delivery.api.domain.token.model.TokenDto;

import java.util.Map;

public interface TokenHelperInterface {

    // Access Token 발급 메서드
    TokenDto issueAccessToken(Map<String, Object> data);

    // Refresh Token 발급 메서드
    TokenDto issueRefreshToken(Map<String, Object> data);

    // 토큰의 유효성을 검증하고, 문제가 있을 경우 예외를 발생시킴
    Map<String, Object> validationTokenWithThrow(String token);

}
