package com.group.ms_travels.core.validation.utils.EnumTypes;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EnumValidatorImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NotNull(message = "Value cannot be null")
@ReportAsSingleViolation
public @interface EnumValidator {

    // The enum class to validate against
    Class <? extends Enum<?>> enumClass();


    // The error message to display if the enum value is invalid
    String message() default "The enum value is invalid";

    // The groups the constraint belongs to
    Class<?>[] groups() default {};

    // The payload the constraint belongs to
    Class<? extends Payload>[] payload() default {};
    }
