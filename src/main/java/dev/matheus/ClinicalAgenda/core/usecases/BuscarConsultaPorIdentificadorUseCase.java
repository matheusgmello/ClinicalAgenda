package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;

public interface BuscarConsultaPorIdentificadorUseCase {
    Consulta execute(String identificador);
}
