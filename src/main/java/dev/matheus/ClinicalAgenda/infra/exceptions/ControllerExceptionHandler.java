package dev.matheus.ClinicalAgenda.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ConsultaDuplicadaException.class)
    public ResponseEntity<Map<String, String>> handleConsultaDuplicada(ConsultaDuplicadaException exception) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("Erro: ", exception.getMessage());
        resposta.put("Mensagem: ", "Por favor, insira um identificador válido para sua consulta e tente novamente.");
        return new ResponseEntity<>(resposta, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConsultaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleConsultaNotFound(ConsultaNotFoundException exception) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("Erro: ", exception.getMessage());
        resposta.put("Mensagem: ", "A consulta solicitada não foi encontrada. Verifique o identificador e tente novamente.");
        return new ResponseEntity<>(resposta, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConsultaDataInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleConsultaDataInvalidaException(ConsultaDataInvalidaException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("Error: ", exception.getMessage());
        response.put("Messagem: ", "A data da consulta deve ser no futuro. Por favor, insira uma data válida e tente novamente.");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }



}
