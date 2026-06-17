package com.integrador.bookifyback.controller;

import com.integrador.bookifyback.domain.autor.exception.AutorNoEncontradoException;
import com.integrador.bookifyback.domain.categoria.exception.CategoriaNoEncontradaException;
import com.integrador.bookifyback.domain.libro.dto.LibroResponse;
import com.integrador.bookifyback.domain.rol.exception.RolNoEncontradoException;
import com.integrador.bookifyback.domain.usuario.dto.LoginResponse;
import com.integrador.bookifyback.domain.usuario.dto.RegisterResponse;
import com.integrador.bookifyback.domain.usuario.exception.CredencialesInvalidasException;
import com.integrador.bookifyback.domain.usuario.exception.CorreoDuplicadoException;
import com.integrador.bookifyback.domain.libro.exception.LibroNoEncontradoException;
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

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<LoginResponse> handleCredencialesInvalidas(CredencialesInvalidasException exception) {
        LoginResponse response = LoginResponse.builder()
                .exito(false)
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(AutorNoEncontradoException.class)
    public ResponseEntity<LibroResponse> handleAutorNoEncontrado(
            AutorNoEncontradoException exception
    ) {

        LibroResponse response = LibroResponse.builder()
                .exito(false)
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<LibroResponse> handleCategoriaNoEncontrada(
            CategoriaNoEncontradaException exception
    ) {

        LibroResponse response = LibroResponse.builder()
                .exito(false)
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(LibroNoEncontradoException.class)
    public ResponseEntity<LibroResponse> handleLibroNoEncontrado(
            LibroNoEncontradoException exception
    ) {

        LibroResponse response = LibroResponse.builder()
                .exito(false)
                .mensaje(exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
