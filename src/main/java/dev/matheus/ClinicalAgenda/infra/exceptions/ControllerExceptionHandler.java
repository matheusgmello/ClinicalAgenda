package dev.matheus.ClinicalAgenda.infra.exceptions;

import dev.matheus.ClinicalAgenda.core.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ConsultaNotFoundException.class)
    public ProblemDetail handleNotFound(ConsultaNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setType(URI.create("https://clinica.com/erros/recurso-nao-encontrado"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errorCode", "CLINICAL-001");
        return problemDetail;
    }

    @ExceptionHandler(ConsultaConflitoHorarioException.class)
    public ProblemDetail handleConflito(ConsultaConflitoHorarioException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Conflito de agenda");
        problemDetail.setType(URI.create("https://clinica.com/erros/conflito-agenda"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errorCode", "CLINICAL-002");
        return problemDetail;
    }

    @ExceptionHandler(ConsultaDataInvalidaException.class)
    public ProblemDetail handleDataInvalida(ConsultaDataInvalidaException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Data ou horário inválido");
        problemDetail.setType(URI.create("https://clinica.com/erros/data-invalida"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errorCode", "CLINICAL-003");
        return problemDetail;
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ProblemDetail handleRegraDeNegocio(RegraDeNegocioException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Violação de regra de negócio");
        problemDetail.setType(URI.create("https://clinica.com/erros/regra-de-negocio"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errorCode", "CLINICAL-000");
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Erro de validação nos campos enviados.");
        problemDetail.setTitle("Campos obrigatórios ou inválidos");
        problemDetail.setType(URI.create("https://clinica.com/erros/validacao-campos"));
        problemDetail.setProperty("errors", errors);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errorCode", "CLINICAL-400");
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno inesperado.");
        problemDetail.setTitle("Erro interno no servidor");
        problemDetail.setType(URI.create("https://clinica.com/erros/erro-interno"));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("errorCode", "CLINICAL-500");
        // Em produção, não retornar ex.getMessage() para evitar vazamento de dados sensíveis.
        return problemDetail;
    }
}
