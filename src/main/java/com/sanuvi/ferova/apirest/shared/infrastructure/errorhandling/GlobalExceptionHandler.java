package com.sanuvi.ferova.apirest.shared.infrastructure.errorhandling;

import com.sanuvi.ferova.apirest.shared.domain.exceptions.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Map;

/**
 * Manejador global de excepciones para toda la aplicación
 * <p>
 *     Esta clase centraliza el manejo de excepciones en toda la aplicación,
 *     proporcionando respuestas HTTP consistentes y amigables para el cliente.
 * </p>
 *
 * @see ControllerAdvice
 * @see ExceptionHandler
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de conversión de mensajes HTTP (JSON inválido)
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseException(HttpMessageNotReadableException ex) {
        String message = "Invalid request format";

        Throwable cause = ex.getCause();
        if(cause instanceof InvalidFormatException ife) {
            var targetType = ife.getTargetType().getSimpleName();
            var invalidValue = ife.getValue();
            message = String.format("Invalid value '%s' for type '%s'.", invalidValue, targetType);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }

    /**
     * Maneja excepciones de validación de argumentos
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Maneja excepciones de credenciales inválidas
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }
}
