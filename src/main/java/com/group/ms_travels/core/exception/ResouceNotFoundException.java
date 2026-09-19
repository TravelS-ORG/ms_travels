package com.group.ms_travels.core.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class ResouceNotFoundException extends RuntimeException {
    public ResouceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of (String entity, String id) {
        return new ResourceNotFoundException(entity + " with id " + id + " not found");
    }
}
