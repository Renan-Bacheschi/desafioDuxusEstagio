package br.com.duxusdesafio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TimeNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleTimeNotFound(TimeNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(PeriodoSemDadosException.class)
    public ResponseEntity<Map<String, String>> handlePeriodoVazio(PeriodoSemDadosException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("aviso", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleDateTypeError(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Formato de data inválido. Use AAAA-MM-DD."));
    }
}