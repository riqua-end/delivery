package org.delivery.api.domain.token.service;

import lombok.RequiredArgsConstructor;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperInterface;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * token 에 대한 도메인 로직
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenHelperInterface tokenHelperInterface;

    // Access Token 발급
    public TokenDto issueAccessToken(Long userId){

        var data = new HashMap<String, Object>();
        data.put("userId", userId);
        return tokenHelperInterface.issueAccessToken(data);
    }

    // Refresh Token 발급
    public TokenDto issueRefreshToken(Long userId){

        var data = new HashMap<String, Object>();
        data.put("userId", userId);
        return tokenHelperInterface.issueRefreshToken(data);
    }

    // Token 의 유효성 검증 및 사용자 ID 추출
    public Long validationToken(String token){

        var map = tokenHelperInterface.validationTokenWithThrow(token);
        var userId = map.get("userId");

        // userId 가 있는지 검사 , 없으면 Null Point 에러
        Objects.requireNonNull(userId, ()->{throw new ApiException(ErrorCode.NULL_POINT);
        });

        // userId를 Long 타입으로 반환
        return Long.parseLong(userId.toString());
    }
}
