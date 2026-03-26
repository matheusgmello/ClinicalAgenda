package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.exceptions.ConsultaConflitoHorarioException;
import dev.matheus.ClinicalAgenda.core.exceptions.ConsultaDataInvalidaException;
import dev.matheus.ClinicalAgenda.core.exceptions.ConsultaNotFoundException;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;

import java.time.Duration;
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

        validarDatas(consulta);
        validarConflitos(consulta, consulta.identificador());

        return consultaGateway.atualizarConsulta(consulta);
    }

    private void validarDatas(Consulta consulta) {
        if (consulta.dataInicio().isBefore(LocalDateTime.now())) {
            throw new ConsultaDataInvalidaException("Não é permitido alterar consultas para datas passadas.");
        }
        if (consulta.dataInicio().isAfter(consulta.dataFim())) {
            throw new ConsultaDataInvalidaException("Data de início não pode ser posterior ao fim.");
        }
        if (Duration.between(consulta.dataInicio(), consulta.dataFim()).toMinutes() < 15) {
            throw new ConsultaDataInvalidaException("A consulta deve ter duração mínima de 15 minutos.");
        }
    }

    private void validarConflitos(Consulta consulta, String identificadorExcluido) {
        if (consultaGateway.existeConflitoMedico(consulta.crmMedico(), consulta.dataInicio(), consulta.dataFim(), identificadorExcluido)) {
            throw new ConsultaConflitoHorarioException("O médico já possui uma consulta agendada neste horário.");
        }
        if (consultaGateway.existeConflitoConsultorio(consulta.consultorio(), consulta.dataInicio(), consulta.dataFim(), identificadorExcluido)) {
            throw new ConsultaConflitoHorarioException("O consultório já está ocupado neste horário.");
        }
    }
}
