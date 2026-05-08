package com.becker.biblioteca.exception;

import com.becker.biblioteca.dto.RespostaErro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorExcecoes {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespostaErro> tratarValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest()
                .body(new RespostaErro("ERRO_VALIDACAO", mensagem));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RespostaErro> tratarNaoEncontrado(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new RespostaErro("NAO_ENCONTRADO", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<RespostaErro> tratarConflito(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new RespostaErro("CONFLITO", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespostaErro> tratarRequisicaoInvalida(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new RespostaErro("REQUISICAO_INVALIDA", ex.getMessage()));
    }
}
