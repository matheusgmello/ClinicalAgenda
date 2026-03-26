package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.exceptions.ConsultaConflitoHorarioException;
import dev.matheus.ClinicalAgenda.core.exceptions.ConsultaDataInvalidaException;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;

import java.time.Duration;
import java.time.LocalDateTime;

public class AgendaConsultaUseCaseImpl implements AgendaConsultaUseCase {

    private final ConsultaGateway consultaGateway;

    public AgendaConsultaUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public Consulta execute(Consulta consulta) {
        validarDatas(consulta);
        validarConflitos(consulta, null);

        String identificador = gerarNovoIdentificador();

        Consulta novaConsulta = new Consulta(
                consulta.id(),
                consulta.pacienteNome(),
                consulta.descricaoSintomas(),
                identificador,
                consulta.dataInicio(),
                consulta.dataFim(),
                consulta.consultorio(),
                consulta.crmMedico(),
                consulta.imgReceitaUrl(),
                consulta.tipo()
        );

        return consultaGateway.agendarConsulta(novaConsulta);
    }

    private void validarDatas(Consulta consulta) {
        if (consulta.dataInicio().isBefore(LocalDateTime.now())) {
            throw new ConsultaDataInvalidaException("Não é permitido agendar consultas para datas passadas.");
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

    private String gerarNovoIdentificador() {
        int anoAtual = LocalDateTime.now().getYear();
        String prefixo = "CONS-" + anoAtual + "-";
        long totalNoAno = consultaGateway.contarConsultasPorPrefixo(prefixo);
        return String.format("%s%03d", prefixo, totalNoAno + 1);
    }
}
