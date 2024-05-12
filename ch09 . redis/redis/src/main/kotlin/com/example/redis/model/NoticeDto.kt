package com.example.redis.model

import java.io.Serializable

data class NoticeDto(
    var id: Long? = 1, // auto increment , auto gen
    var notice: String? = null, // notice
) : Serializable // 직렬화를 시켜줘야함
{

}