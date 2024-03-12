package org.delivery.api.common.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Api 클래스: 응답 데이터를 담는 객체
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> {

    // 응답 결과 코드
    private Result result;

    // 응답 데이터
    @Valid
    private T body;

    // 성공 응답을 생성하는 정적 메서드
    public static <T> Api<T> OK(T data){
        // Api 객체 생성
        var api = new Api<T>();

        // 결과 코드를 OK로 설정
        api.result = Result.OK();

        // 응답 데이터 설정
        api.body = data;

        // Api 객체 반환
        return api;
    }

}

