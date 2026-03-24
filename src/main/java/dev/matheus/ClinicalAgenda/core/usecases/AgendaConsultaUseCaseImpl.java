package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaDataInvalidaException;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaDuplicadaException;

import java.time.LocalDateTime;

public class AgendaConsultaUseCaseImpl implements AgendaConsultaUseCase {

    private final ConsultaGateway consultaGateway;

    public AgendaConsultaUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public Consulta execute(Consulta consulta) {
        if (consulta.dataInicio().isBefore(LocalDateTime.now())) {
            throw new ConsultaDataInvalidaException("Não é permitido agendar consultas para datas passadas.");
        }
        if (consulta.dataInicio().isAfter(consulta.dataFim())) {
            throw new ConsultaDataInvalidaException("Data de início não pode ser posterior ao fim.");
        }

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

    private String gerarNovoIdentificador() {
        int anoAtual = LocalDateTime.now().getYear();
        String prefixo = "CONS-" + anoAtual + "-";
        long totalNoAno = consultaGateway.contarConsultasPorPrefixo(prefixo);
        return String.format("%s%03d", prefixo, totalNoAno + 1);
    }
}
