package org.delivery.api.config.objectmapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*@Configuration
public class ObjectMapperConfig {

    @Bean // ObjectMapper 객체를 Spring Bean으로 등록. 즉 애플리케이션 전반에 ObjectMapper를 주입받아 사용가능.
    public ObjectMapper objectMapper(){
        var objectMapper = new ObjectMapper();

        // ===== 모듈 등록 =====
        objectMapper.registerModule(new Jdk8Module()); // Java 8 시간/Optional 등의 지원
        objectMapper.registerModule(new JavaTimeModule()); // Java 8 시간 타입 지원 (LocalDate, LocalDateTime 등)

        // ===== Deserialization 관련 설정 =====
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //  - JSON에 정의되지 않은 속성이 있어도 에러내지 않고 무시하고 역직렬화 진행.

        // ===== Serialization 관련 설정 =====
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //  - 속성이 모두 비어있는 객체에 대해서는 JSON으로 내보내지 않음.

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //  - 날짜 타입을 타임스탬프가 아닌, 문자열 형식으로 직렬화

        // ===== Property Naming 관련 설정 =====
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        //  - JSON 키를 스네이크 케이스(snake_case)로 매핑. Ex) "deliveryDate" -> "delivery_date"

        return objectMapper;
    }
}*/
