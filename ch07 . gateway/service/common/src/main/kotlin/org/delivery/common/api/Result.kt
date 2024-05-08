package org.delivery.common.api

import org.delivery.common.error.ErrorCode
import org.delivery.common.error.ErrorCodeInterface

data class Result(
    val resultCode: Int?=null,
    val resultMessage: String?=null,
    val resultDescription: String?=null,
) {

    companion object { // Java 의 static 과 동일하게 사용
        @JvmStatic
        fun OK(): Result {
            return Result(
                resultCode = ErrorCode.OK.getErrorCode(),
                resultMessage = ErrorCode.OK.getDescription(),
                resultDescription = "성공"
            )
        }
        @JvmStatic
        fun ERROR(errorCodeInterface: ErrorCodeInterface): Result {
            return Result(
                resultCode = errorCodeInterface.getErrorCode(),
                resultMessage = errorCodeInterface.getDescription(),
                resultDescription = "에러 발생"
            )
        }
        @JvmStatic
        fun ERROR(errorCodeInterface: ErrorCodeInterface, tx: Throwable): Result {
            return Result(
                resultCode = errorCodeInterface.getErrorCode(),
                resultMessage = errorCodeInterface.getDescription(),
                resultDescription = tx.localizedMessage
            )
        }
        @JvmStatic
        fun ERROR(errorCodeInterface: ErrorCodeInterface, description: String): Result {
            return Result(
                resultCode = errorCodeInterface.getErrorCode(),
                resultMessage = errorCodeInterface.getDescription(),
                resultDescription = description
            )
        }
    }
}