package dev.matheus.ClinicalAgenda.core.exceptions;

public class ConsultaNotFoundException extends RegraDeNegocioException {
    public ConsultaNotFoundException(String message) {
        super(message);
    }
}
