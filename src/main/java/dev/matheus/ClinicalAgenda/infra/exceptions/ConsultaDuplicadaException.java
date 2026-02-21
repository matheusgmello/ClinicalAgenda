package dev.matheus.ClinicalAgenda.infra.exceptions;

public class ConsultaDuplicadaException extends RuntimeException{
    public ConsultaDuplicadaException(String message) {
        super(message);
    }
}
