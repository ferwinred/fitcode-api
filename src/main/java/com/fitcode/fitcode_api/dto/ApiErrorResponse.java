package com.fitcode.fitcode_api.dto;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String code,
        String message,
        String path,
        Map<String, String> details) {

    public static ApiErrorResponse of(int status, String code, String message, String path) {
        return new ApiErrorResponse(Instant.now(), status, code, message, path, Map.of());
    }

    public static ApiErrorResponse of(
            int status,
            String code,
            String message,
            String path,
            Map<String, String> details) {
        return new ApiErrorResponse(Instant.now(), status, code, message, path, details);
    }
}
