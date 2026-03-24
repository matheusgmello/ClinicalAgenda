package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaDataInvalidaException;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaNotFoundException;

import java.time.LocalDateTime;

public class AlterarConsultaUseCaseImpl implements AlterarConsultaUseCase {

    private final ConsultaGateway consultaGateway;

    public AlterarConsultaUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public Consulta execute(Consulta consulta) {
        if (!consultaGateway.existePorIdentificador(consulta.identificador())) {
            throw new ConsultaNotFoundException("Consulta não encontrada com o identificador: " + consulta.identificador());
        }

        if (consulta.dataInicio().isBefore(LocalDateTime.now())) {
            throw new ConsultaDataInvalidaException("Não é permitido agendar/alterar consultas para datas passadas.");
        }
        if (consulta.dataInicio().isAfter(consulta.dataFim())) {
            throw new ConsultaDataInvalidaException("Data de início não pode ser posterior ao fim.");
        }

        return consultaGateway.atualizarConsulta(consulta);
    }
}
