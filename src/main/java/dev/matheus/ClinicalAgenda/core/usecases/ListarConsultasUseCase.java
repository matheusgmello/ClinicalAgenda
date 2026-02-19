package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;

import java.util.List;

public interface ListarConsultasUseCase {

    List<Consulta> executar();
}
