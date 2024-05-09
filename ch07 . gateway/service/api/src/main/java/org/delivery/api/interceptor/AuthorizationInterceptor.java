package org.delivery.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.error.TokenErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Entered AuthorizationInterceptor.preHandle");
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // WEB , chrome 의 경우 GET , POST OPTIONS = pass
        if (HttpMethod.OPTIONS.matches(request.getMethod())){
            return true;
        }

        // js. html. png resource 를 요청하는 경우 = pass
        if (handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        var userId = request.getHeader("x-user-id");

        if (userId == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "x-user-id header 없음");
        }

//        var userId = tokenBusiness.validationAccessToken(accessToken);

//        if (userId != null) {
            // 한가지 요청에 대해 글로벌하게 저장 할 수 있는 저장소에 저장
            var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());

            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);

            return true;
//        }

//        throw new ApiException(ErrorCode.BAD_REQUEST, "인증 실패");
    }
}
