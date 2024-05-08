package org.delivery.common.exception

import org.delivery.common.error.ErrorCodeInterface

interface ApiExceptionInterface {
    val errorCodeInterface: ErrorCodeInterface?
    val errorDescription: String?
}