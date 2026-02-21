package dev.matheus.ClinicalAgenda.infra.exceptions;

public class ConsultaDataInvalidaException extends RuntimeException {

    public ConsultaDataInvalidaException(String message) {
        super(message);
    }
}
