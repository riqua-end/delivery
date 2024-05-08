package org.delivery.common.error

interface ErrorCodeInterface {

    fun getHttpStatusCode(): Int
    fun getErrorCode():Int
    fun getDescription():String
}