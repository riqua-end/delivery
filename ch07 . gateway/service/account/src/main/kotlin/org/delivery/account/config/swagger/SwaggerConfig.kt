package org.delivery.account.config.swagger

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.core.jackson.ModelResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun modelResolver(
        objectMapper: ObjectMapper
    ): ModelResolver {
        return ModelResolver(objectMapper)
    }

    /*  SwaggerConfig 클래스는 Spring Boot 애플리케이션에서 Swagger를 위한 설정을 담당합니다.
        modelResolver 메소드는 Swagger 문서에 사용될 모델을 생성하는 ModelResolver 객체를 생성하고 반환합니다.
        ModelResolver 객체는 Jackson의 ObjectMapper를 사용하여 Java 객체를 JSON 모델로 변환합니다.
    */
}