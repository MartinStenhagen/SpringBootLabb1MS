package org.example.springbootlabb1ms.book.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsbnValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Isbn {
    String message() default "isbn must be a valid isbn-10 or isbn-13";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
