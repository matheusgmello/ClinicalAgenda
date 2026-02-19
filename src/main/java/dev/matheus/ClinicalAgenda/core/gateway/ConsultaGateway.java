package dev.matheus.ClinicalAgenda.core.gateway;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;

import java.util.List;
import java.util.Optional;

public interface ConsultaGateway {
    Consulta agendarConsulta(Consulta consulta);
    List<Consulta> listarTodasConsultas();
    Optional<Consulta> buscarConsultaPorIdentificador(String identificador);
    boolean existePorIdentificador(String identificador);
    void cancelarConsulta(String identificador);
}