package dev.lucasmattos.cooperative_vote.core.usecase.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UseCaseException extends RuntimeException {
    public final String errorCode;
    public final HttpStatus httpStatus;

    public UseCaseException(final String errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public UseCaseException(final String errorCode, final HttpStatus httpStatus, final String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
