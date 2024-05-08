package org.delivery.common.exception

import org.delivery.common.error.ErrorCodeInterface

class ApiException : RuntimeException, ApiExceptionInterface {

    override val errorCodeInterface: ErrorCodeInterface
    override val errorDescription: String

    constructor(errorCodeInterface: ErrorCodeInterface) : super(errorCodeInterface.getDescription()){
        this.errorCodeInterface = errorCodeInterface
        this.errorDescription = errorCodeInterface.getDescription()
    }

    constructor(
        errorCodeInterface: ErrorCodeInterface,
        errorCodeDescription: String,
    ): super(errorCodeDescription){
        this.errorCodeInterface = errorCodeInterface
        this.errorDescription = errorCodeDescription
    }

    constructor(
        errorCodeInterface: ErrorCodeInterface,
        throwable: Throwable
    ): super(throwable){
        this.errorCodeInterface = errorCodeInterface
        this.errorDescription = errorCodeInterface.getDescription()
    }

    constructor(
        errorCodeInterface: ErrorCodeInterface,
        throwable: Throwable,
        errorCodeDescription: String
    ): super(throwable){
        this.errorCodeInterface = errorCodeInterface
        this.errorDescription = errorCodeDescription
    }
}