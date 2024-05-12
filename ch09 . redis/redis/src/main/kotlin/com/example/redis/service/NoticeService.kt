package com.example.redis.service

import com.example.redis.common.Log
import com.example.redis.repository.NoticeRepository
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository
) {
    companion object: Log

    @Cacheable(cacheNames = ["notice"], key = "#notice")
    fun getNotice(notice: String?) : String?{
        log.info("notice service get notice : {}", notice)
        return noticeRepository.getNotice(notice)
    }

    @CachePut(cacheNames = ["notice"], key = "#notice") // #notice 는 매개변수의 notice
    fun addNotice(notice: String?) : String?{
        log.info("notice add service notice : {}", notice)
        return noticeRepository.addNotice(notice)
    }
}