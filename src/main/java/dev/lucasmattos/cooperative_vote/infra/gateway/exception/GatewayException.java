package dev.lucasmattos.cooperative_vote.infra.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GatewayException extends RuntimeException {
    public final String errorCode;
    public final HttpStatus httpStatus;

    public GatewayException(final String errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public GatewayException(final String errorCode, final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
