package dev.lucasmattos.cooperative_vote.infra.config.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorDetail {

    String object;
    String field;
    String path;
    String message;

    public ApiErrorDetail(final String message) {
        this.message = message;
    }

    public ApiErrorDetail(final ConstraintViolation<?> constraintViolation) {
        this.message = constraintViolation.getMessage();
        if (constraintViolation.getPropertyPath() instanceof PathImpl propertyPath) {
            this.field = propertyPath.getLeafNode().getName();
        }
    }

    public ApiErrorDetail(final ObjectError objectError) {
        this.message = objectError.getDefaultMessage();
        this.object = objectError.getObjectName();

        if (objectError instanceof FieldError) {
            this.field = ((FieldError) objectError).getField();
        }
    }
}
