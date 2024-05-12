package com.example.redis.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching // 캐싱된 데이터 사용
class RedisConfig {

}