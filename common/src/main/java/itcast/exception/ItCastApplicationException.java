package itcast.exception;

import org.springframework.http.HttpStatus;

public class ItCastApplicationException extends RuntimeException {

    private final ErrorCodes errorCodes;

    public ItCastApplicationException(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
    }

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public HttpStatus getHttpStatus() {
        return errorCodes.status;
    }
}
