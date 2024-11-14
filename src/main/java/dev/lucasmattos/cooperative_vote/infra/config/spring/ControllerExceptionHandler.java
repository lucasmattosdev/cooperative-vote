package dev.lucasmattos.cooperative_vote.infra.config.spring;

import dev.lucasmattos.cooperative_vote.core.usecase.exception.UseCaseException;
import dev.lucasmattos.cooperative_vote.infra.gateway.exception.GatewayException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(UseCaseException.class)
    public ResponseEntity<ApiError> handleUseCaseException(final UseCaseException exception) {
        LOGGER.warn("Use case exception", exception);

        final ApiError apiError = new ApiError(
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getHttpStatus().value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<ApiError> handleUseCaseException(final GatewayException exception) {
        LOGGER.warn("Gateway exception", exception);

        final ApiError apiError = new ApiError(
                exception.getErrorCode(),
                exception.getMessage(),
                exception.getHttpStatus().value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(
            final NoResourceFoundException noResourceFoundException) {
        LOGGER.error("No resource found", noResourceFoundException);

        final ApiError apiError =
                new ApiError("no_resource_found", "No resource found for this path", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        LOGGER.error("Method Argument Type Mismatch", methodArgumentTypeMismatchException);

        final ApiError apiError = new ApiError(
                "method_argument_type_mismatch",
                "The request contains an invalid value",
                HttpStatus.BAD_REQUEST.value(),
                methodArgumentTypeMismatchException.getMessage());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler({MissingRequestValueException.class})
    public ResponseEntity<ApiError> handleMissingServletRequestParameterException(
            final MissingRequestValueException missingRequestValueException) {
        LOGGER.error("Missing Request Value", missingRequestValueException);

        final ApiError apiError = new ApiError(
                "missing_request_value",
                "The request is missing a required value",
                HttpStatus.BAD_REQUEST.value(),
                missingRequestValueException.getMessage());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException httpMessageNotReadableException) {
        LOGGER.error("Body unreadable", httpMessageNotReadableException);

        final ApiError apiError = new ApiError(
                "body_unreadable",
                "The body request contains an unreadable value",
                HttpStatus.BAD_REQUEST.value(),
                httpMessageNotReadableException.getMessage());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(
            final ConstraintViolationException constraintViolationException) {
        LOGGER.error("Constraint Violation", constraintViolationException);

        final ApiError apiError = new ApiError(
                "constraint_violation",
                "The request contains an invalid value",
                HttpStatus.BAD_REQUEST.value(),
                constraintViolationException.getConstraintViolations());

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnknownException(final Exception exception) {
        LOGGER.error("Internal error", exception);

        final ApiError apiError =
                new ApiError("internal_error", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
