package dev.matheus.ClinicalAgenda.infra.exceptions;

public class ConsultaNotFoundException extends RuntimeException {

    public ConsultaNotFoundException(String message) {
        super(message);
    }
}
