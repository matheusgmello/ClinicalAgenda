package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;

import java.util.List;

public class ListarConsultasUseCaseImpl implements ListarConsultasUseCase {
    private final ConsultaGateway consultaGateway;

    public ListarConsultasUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public List<Consulta> execute() {
        return consultaGateway.listarTodasConsultas();
    }
}
