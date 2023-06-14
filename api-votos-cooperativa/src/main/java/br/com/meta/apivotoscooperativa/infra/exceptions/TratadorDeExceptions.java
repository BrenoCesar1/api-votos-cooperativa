package br.com.meta.apivotoscooperativa.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeExceptions {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity error404() { return ResponseEntity.notFound().build(); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity error400(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors();
        return ResponseEntity.badRequest().body(errors.stream().map(DadosErroValidacao::new).toList());
    }

    @Data
    private class DadosErroValidacao {
        private String campo;
        private String mensagem;

        public DadosErroValidacao(FieldError error) {
            this.campo = error.getField();
            this.mensagem = error.getDefaultMessage();
        }
    }
}
