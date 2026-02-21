package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaNotFoundException;

public class BuscarConsultaPorIdentificadorUseCaseImpl implements BuscarConsultaPorIdentificadorUseCase {

    private final ConsultaGateway consultaGateway;

    public BuscarConsultaPorIdentificadorUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public Consulta execute(String identificador) {
        return consultaGateway.buscarConsultaPorIdentificador(identificador)
                .orElseThrow(() -> new ConsultaNotFoundException("Consulta não encontrada."));
    }
}
