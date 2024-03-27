package org.delivery.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Slf4j
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 요청 데이터를 캐싱하여 요청 본문을 포함한 모든 요청 정보를 캡처함
        var req = new ContentCachingRequestWrapper((HttpServletRequest) request);
        // 응답 데이터를 캐싱하여 응답 본문을 포함한 모든 응답 정보를 캡처함
        var res = new ContentCachingResponseWrapper((HttpServletResponse) response);

        log.info("INIT URI : {} ", req.getRequestURI());
        // 필터 체인 진행
        chain.doFilter(req, res);

        // request 정보
        var headerNames = req.getHeaderNames(); // 모든 헤더의 이름을 가져옴
        var headerValues = new StringBuilder(); // 각 헤더의 이름과 값을 StringBuilder에 추가

        // 헤더 추가
        headerNames.asIterator().forEachRemaining(headerKey ->{ // 각 헤더 이름을 반복함
            var headerValue = req.getHeader(headerKey);

            // ex>> authorization-token : ??? , user-agent : ???
            // headerKey와 headerValue를 사용해서 각 헤더 정보를 문자열에 추가
            headerValues.append("[").append(headerKey).append(" : ").append(headerValue).append("] ").append(" , ");
        });

        // 요청 정보 가져오기
        var requestBody = new String(req.getContentAsByteArray()); // 요청 본문을 바이트 배열로 가져옴

        var uri = req.getRequestURI();
        var method = req.getMethod();

        log.info(">>>> uri : {} , method : {} , header : {} , body : {} ",uri, method, headerValues, requestBody);

        // response 정보
        var responseHeaderValues = new StringBuilder();

        res.getHeaderNames().forEach(headerKey -> {
            var headerValue = res.getHeader(headerKey);

            responseHeaderValues.append("[").append(headerKey).append(" : ").append(headerValue).append("] ");
        });

        // 응답 정보 가져오기
        var responseBody = new String(res.getContentAsByteArray());

        log.info("<<<< uri : {} , method : {} , header : {} , body : {}",uri , method, responseHeaderValues, responseBody);

        // 응답 본문을 다시 출력으로 복사
        res.copyBodyToResponse();
    }
}
