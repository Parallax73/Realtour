package br.com.realtour.util;

import java.time.LocalDateTime;



public record ErrorResponse(
        LocalDateTime timestamp,
        String message,
        String details) {
}
