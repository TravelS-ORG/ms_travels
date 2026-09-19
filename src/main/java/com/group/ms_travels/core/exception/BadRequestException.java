package com.group.ms_travels.core.exception;

import java.util.List;

public class BadRequestException extends RuntimeException {

    private final List<FieldError> errors;

    public BadRequestException(String message) {
        super(message);
        this.errors = List.of();
    }

    public BadRequestException(String message, List<FieldError> errors) {
        super(message);
        this.errors = errors;
    }

    public record FieldError(String field, String value, String issue) {}
}
