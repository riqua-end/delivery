package org.delivery.api.domain.token.helper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.error.TokenErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.ifs.TokenHelperInterface;
import org.delivery.api.domain.token.model.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component // Spring 컨테이너에서 관리하는 Bean 으로 등록
public class JwtTokenHelper implements TokenHelperInterface {
    // application.yaml 의 키 : 값을 @Value("${}") 로 주입
    @Value("${token.secret.key}")
    private String secretKey;
    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;
    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    /**
     * Access Token 발급 메서드
     */
    @Override
    public TokenDto issueAccessToken(Map<String, Object> data) {
        // 1. 현재 시간 + 토큰의 만료 시간 계산
        var expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);
        // 1-1. 시스템 기본 시간대를 사용하여 LocalDateTime을 Date로 변환
        // LocalDateTime 은 시간 정보만 가지고 있고 시간대 정보를 가지고 있지 않음.
        // atZone() 메서드는 시스템 기본 시간대 정보를 반환함
        // expiredLocalDateTime 객체를 시스템 기본 시간대를 기준으로 Date 객체로 변환.
        var expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        // 2. 토큰 서명을 위한 키 생성
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // 3.JWT 토큰 생성
        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256) // 토큰의 서명 생성
                .setClaims(data) // 사용자 정보 등 토큰에 담을 데이터 설정
                .setExpiration(expiredAt) // 토큰의 만료 시간 설정
                .compact();

        // 4. TokenDto 객체 생성 및 반환
        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    /**
     * RefreshToken 발급 메서드 ( AccessToken 메서드와 비슷 )
     */
    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {
        // refreshTokenPlusHour 로 변경
        var expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);

        var expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        var jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();
    }

    /**
     * 토큰 검증 메서드
     */
    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {

        // 1. 토큰 서명 검증을 위한 키 생성
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // 2. 토큰 파서 생성
        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {
            // 3. 토큰 파싱 및 정보 추출
            var result = parser.parseClaimsJws(token);
            // 4. 토큰 정보를 Map 으로 변환하여 반환
            return new HashMap<String, Object>(result.getBody());
        }
        catch (Exception e){
            if (e instanceof SignatureException){
                // 토큰이 유효하지 않을 때 예외 처리
                throw new ApiException(TokenErrorCode.INVALID_TOKEN, e);
            } else if (e instanceof ExpiredJwtException) {
                // 만료된 토큰의 예외 처리
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN, e);
            }
            else {
                // 그 외 에러
                throw new ApiException(TokenErrorCode.TOKEN_EXCEPTION, e);
            }
        }

    }
}
