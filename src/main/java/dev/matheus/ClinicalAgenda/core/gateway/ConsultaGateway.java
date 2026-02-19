package dev.matheus.ClinicalAgenda.core.gateway;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;

import java.util.List;
import java.util.Optional;

public interface ConsultaGateway {
    Consulta agendar(Consulta consulta);
    List<Consulta> listarTodas();
    Optional<Consulta> buscarPorIdentificador(String identificador);
    boolean existePorIdentificador(String identificador);
    void excluir(String identificador);
}