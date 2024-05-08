package org.delivery.api.config.objectmapper

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        // kotlin module 가져오기
        val kotlinModule = KotlinModule.Builder().apply {
            withReflectionCacheSize(512)
            configure(KotlinFeature.NullToEmptyCollection, false) // true 면 사이즈가 0인 컬렉션, false 는 default 가 null 로 초기화
            configure(KotlinFeature.NullToEmptyMap, false) // 위와 마찬가지로 데이터가 없으면 null
            configure(KotlinFeature.NullIsSameAsDefault, false)
            configure(KotlinFeature.SingletonSupport, false)
            configure(KotlinFeature.StrictNullChecks, false)
        }.build()

        val objectMapper = ObjectMapper().apply {
            registerModules(Jdk8Module())
            registerModules(JavaTimeModule())
            registerModules(kotlinModule)

            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false)

            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

            propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
        }

        return objectMapper
    }
}