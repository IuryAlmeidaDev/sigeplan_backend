package com.sejus.sigeplan.interfaces.rest.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return build(HttpStatus.BAD_REQUEST, "Dados inválidos.", details);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.", List.of());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiErrorResponse> handleDisabledUser(DisabledException ex) {
        return build(HttpStatus.FORBIDDEN, "Usuário inativo.", List.of());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return build(status, ex.getReason(), List.of());
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(
            org.springframework.web.HttpRequestMethodNotSupportedException ex
    ) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não suportado para este endpoint.", List.of());
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException ex
    ) {
        return build(HttpStatus.FORBIDDEN, "Acesso negado.", List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor.",
                List.of(ex.getClass().getSimpleName())
        );
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, List<String> details) {
        ApiErrorResponse response = new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                details
        );

        return ResponseEntity.status(status).body(response);
    }
}