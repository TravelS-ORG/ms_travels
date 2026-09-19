package com.group.ms_travels.core.validation.utils.EnumTypes;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    private List<String> validValues;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        validValues = new ArrayList<>();
        for (Enum<?> e: constraintAnnotation.enumClass().getEnumConstants()) {
            validValues.add(e.name());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return validValues.contains(value.toUpperCase());
    }
}
