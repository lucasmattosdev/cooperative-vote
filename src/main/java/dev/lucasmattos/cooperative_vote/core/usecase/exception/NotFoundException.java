package dev.lucasmattos.cooperative_vote.core.usecase.exception;

import static java.lang.String.format;

import java.util.Locale;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public class NotFoundException extends UseCaseException {
    public NotFoundException(final Class<?> clazz, final UUID id) {
        super(
                clazz.getSimpleName().replaceAll("(.)([A-Z])", "$1-$2").toLowerCase(Locale.ROOT) + "-not-found",
                HttpStatus.NOT_FOUND,
                format("The %s with id %s was not found", clazz.getSimpleName().replaceAll("(.)([A-Z])", "$1 $2"), id));
    }
}
