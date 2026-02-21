package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaNotFoundException;

public class CancelarConsultaUseCaseImpl implements CancelarConsultaUseCase{

    private final ConsultaGateway consultaGateway;

    public CancelarConsultaUseCaseImpl(ConsultaGateway consultaGateway) {
        this.consultaGateway = consultaGateway;
    }

    @Override
    public void execute(String identificador) {
        if (!consultaGateway.existePorIdentificador(identificador)) {
            throw new ConsultaNotFoundException("Impossível cancelar: consulta não encontrada.");
        }
        consultaGateway.cancelarConsulta(identificador);
    }
}
