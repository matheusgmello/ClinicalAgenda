package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;

public class AgendaConsultaUseCaseImpl implements AgendaConsultaUseCase {

    private final ConsultaGateway consultaGateway;

    public AgendaConsultaUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public Consulta execute(Consulta consulta) {
        if (consulta.dataInicio().isAfter(consulta.dataFim())) {
            throw new RuntimeException("Data de início não pode ser posterior ao fim.");
        }
        if (consultaGateway.existePorIdentificador(consulta.identificador())) {
            throw new IllegalArgumentException("Já existe uma consulta com o mesmo identificador.");
        }
        return consultaGateway.agendarConsulta(consulta);
    }
}
