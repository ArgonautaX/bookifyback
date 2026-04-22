package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.rol.exception.RolNoEncontradoException;
import com.integrador.bookifyback.domain.usuario.dto.RegisterResponse;
import com.integrador.bookifyback.domain.usuario.exception.CorreoDuplicadoException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<RegisterResponse> handleCorreoDuplicado(CorreoDuplicadoException exception) {
        RegisterResponse response = RegisterResponse.builder()
                .exito(false)
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RegisterResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        String mensaje = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        RegisterResponse response = RegisterResponse.builder()
                .exito(false)
                .mensaje(mensaje)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RolNoEncontradoException.class)
    public ResponseEntity<RegisterResponse> handleRolNoEncontrado(RolNoEncontradoException exception) {
        RegisterResponse response = RegisterResponse.builder()
                .exito(false)
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
