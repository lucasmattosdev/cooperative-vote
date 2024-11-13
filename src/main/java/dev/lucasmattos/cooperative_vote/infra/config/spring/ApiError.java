package dev.lucasmattos.cooperative_vote.infra.config.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.ObjectError;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    Integer status;

    String error;

    String message;

    List<ApiErrorDetail> details;

    public ApiError(final String error, final String message, final Integer status) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ApiError(
            final String error, final String message, final Integer status, final List<ObjectError> objectError) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.details = objectError.stream().map(ApiErrorDetail::new).toList();
    }

    public ApiError(
            final String error,
            final String message,
            final Integer status,
            final Set<ConstraintViolation<?>> constraintViolations) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.details = constraintViolations.stream().map(ApiErrorDetail::new).toList();
    }

    public ApiError(final String error, final String message, final Integer status, final String details) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.details = List.of(new ApiErrorDetail(details));
    }
}
