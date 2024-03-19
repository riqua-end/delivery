package org.delivery.api.domain.token.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.api.domain.token.controller.model.TokenResponse;
import org.delivery.api.domain.token.converter.TokenConverter;
import org.delivery.api.domain.token.service.TokenService;
import org.delivery.db.BaseEntity;
import org.delivery.db.user.UserEntity;

import java.util.Optional;

@Business
@RequiredArgsConstructor
public class TokenBusiness {

    private final TokenService tokenService;

    private final TokenConverter tokenConverter;

    /**
     * 1. user entity user id 추출
     * 2. access, refresh token 발행
     * 3. converter -> token response 로 변경
     */

    public TokenResponse issueToken(
            UserEntity userEntity
    ){
        // 사용자 ID 추출
        return Optional.ofNullable(userEntity)
                .map(UserEntity::getId) // 사용자 ID 추출 , 람다식 메서드 참조
                .map(userId ->{ // 사용자 ID 로 토큰 발급
                    var accessToken = tokenService.issueAccessToken(userId);
                    var refreshToken = tokenService.issueRefreshToken(userId);
                    return tokenConverter.toResponse(accessToken, refreshToken);
                })
                .orElseThrow(()-> new ApiException(ErrorCode.NULL_POINT));
    }

    public Long validationAccessToken(String accessToken){

        return tokenService.validationToken(accessToken);
    }
}
