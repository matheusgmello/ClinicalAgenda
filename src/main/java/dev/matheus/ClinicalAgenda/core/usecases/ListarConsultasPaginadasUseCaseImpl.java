package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.dtos.PaginaResponse;
import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;

public class ListarConsultasPaginadasUseCaseImpl implements ListarConsultasPaginadasUseCase {

    private final ConsultaGateway consultaGateway;

    public ListarConsultasPaginadasUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public PaginaResponse<Consulta> execute(int pagina, int tamanho) {
        return consultaGateway.listarPaginado(pagina, tamanho);
    }
}
