package dev.matheus.ClinicalAgenda.core.gateway;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaGateway {
    Consulta agendarConsulta(Consulta consulta);
    List<Consulta> listarTodasConsultas();
    boolean existePorIdentificador(String identificador);
    Optional<Consulta> buscarConsultaPorIdentificador(String identificador);
    void cancelarConsulta(String identificador);
    long contarConsultasPorPrefixo(String prefixo);
    Consulta atualizarConsulta(Consulta consulta);

    boolean existeConflitoMedico(String crmMedico, LocalDateTime inicio, LocalDateTime fim, String identificadorExcluido);
    boolean existeConflitoConsultorio(String consultorio, LocalDateTime inicio, LocalDateTime fim, String identificadorExcluido);
}