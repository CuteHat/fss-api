package com.example.fssapi.validation;

import com.example.fssapi.exception.ClientInformingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public final class JakartaValidationUtil {
    private static final ValidatorFactory defaultValidatorFactory = Validation.buildDefaultValidatorFactory();
    private static final Validator defaultValidator = defaultValidatorFactory.getValidator();

    private JakartaValidationUtil() {
    }

    public static <T> Set<ConstraintViolation<T>> getViolations(T object) {
        return defaultValidator.validate(object);
    }

    public static <T> void throwIfViolations(T object) {
        Set<ConstraintViolation<T>> violations = getViolations(object);
        if (violations.isEmpty()) {
            return;
        }
        log.debug("found following violations: {}", violations);
        throw new ClientInformingException(createInformingMessageFrom(violations));
    }

    public static <T> String createInformingMessageFrom(Set<ConstraintViolation<T>> violations) {
        StringBuilder sb = new StringBuilder();
        sb.append("found following violations in your request: ");
        for (ConstraintViolation<?> violation : violations) {
            sb.append("(");
            sb.append(violation.getPropertyPath().toString());
            sb.append(": ");
            sb.append(violation.getMessage());
            sb.append(")");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
