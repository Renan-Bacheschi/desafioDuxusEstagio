package br.com.duxusdesafio.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TimeNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleTimeNotFound(TimeNaoEncontradoException ex) {
        logger.error("ERRO: Time não encontrado. Detalhes: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", ex.getMessage()));
    }

    @ExceptionHandler(PeriodoSemDadosException.class)
    public ResponseEntity<Map<String, String>> handlePeriodoVazio(PeriodoSemDadosException ex) {
        logger.warn("AVISO: Período solicitado está vazio.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("aviso", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleDateTypeError(MethodArgumentTypeMismatchException ex) {
        logger.error("ERRO DE TIPO: O usuário digitou uma data inválida: {}", ex.getValue());

        return ResponseEntity.badRequest().body(Map.of("erro", "Formato de data inválido. Use AAAA-MM-DD."));
    }

    @ExceptionHandler(SemDataException.class)
    public ResponseEntity<Map<String, String>> handleSemData(SemDataException ex) {
        logger.error("REQUISIÇÃO INVÁLIDA: O parâmetro 'data' foi esquecido.", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", ex.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParam(MissingServletRequestParameterException ex) {
        logger.error("Falha na requisição: Parâmetro obrigatório ausente: {}", ex.getParameterName());

        return ResponseEntity.badRequest().body(Map.of(
                "erro", "Dados insuficientes",
                "mensagem", "Você precisa informar uma data valida encontrar o time!"
        ));
    }
}