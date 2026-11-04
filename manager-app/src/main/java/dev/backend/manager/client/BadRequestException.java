package dev.backend.manager.client;

import java.util.HashMap;
import java.util.Map;

public class BadRequestException extends RuntimeException {
    private final Map<String, String> errors;

    public BadRequestException() {
        this.errors = new HashMap<>(Map.of("errors", "Unknown"));
    }

    public BadRequestException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
