package org.delivery.api.common.exception;

import org.delivery.api.common.error.ErrorCodeInterface;

public interface ApiExceptionInterface {

    ErrorCodeInterface getErrorCodeInterface();

    String getErrorDescription();
}
