package com.orness.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used to specify that an email in a payload should be unique in the database
 */
@Documented
@Constraint(validatedBy = UniqueMailValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface UniqueEmail {
    String message() default "Email address already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
