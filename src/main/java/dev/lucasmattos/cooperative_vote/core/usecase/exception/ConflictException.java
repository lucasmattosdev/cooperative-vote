package dev.lucasmattos.cooperative_vote.core.usecase.exception;

import java.util.Locale;
import org.springframework.http.HttpStatus;

public class ConflictException extends UseCaseException {
    public ConflictException(final Class clazz, final String message) {
        super(
                "conflict-"
                        + clazz.getSimpleName()
                                .replaceAll("(.)([A-Z])", "$1-$2")
                                .toLowerCase(Locale.ROOT),
                HttpStatus.CONFLICT,
                message);
    }
}
