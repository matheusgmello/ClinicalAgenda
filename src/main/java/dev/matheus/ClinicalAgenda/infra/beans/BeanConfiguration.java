package dev.matheus.ClinicalAgenda.infra.beans;

import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.core.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public AgendaConsultaUseCase agendaConsultaUseCase(ConsultaGateway consultaGateway) {
        return new AgendaConsultaUseCaseImpl(consultaGateway);
    }

    @Bean
    public ListarConsultasUseCase listarConsultasUseCase(ConsultaGateway consultaGateway) {
        return new ListarConsultasUseCaseImpl(consultaGateway);
    }

    @Bean
    public BuscarConsultaPorIdentificadorUseCase buscarConsultaPorIdentificadorUseCase(ConsultaGateway consultaGateway) {
        return new BuscarConsultaPorIdentificadorUseCaseImpl(consultaGateway);
    }

    @Bean
    public CancelarConsultaUseCase cancelarConsultaUseCase(ConsultaGateway consultaGateway) {
        return new CancelarConsultaUseCaseImpl(consultaGateway);
    }

}
