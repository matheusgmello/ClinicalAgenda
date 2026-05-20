package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.dtos.PaginaResponse;
import dev.matheus.ClinicalAgenda.core.entities.Consulta;

public interface ListarConsultasPaginadasUseCase {
    PaginaResponse<Consulta> execute(int pagina, int tamanho);
}
