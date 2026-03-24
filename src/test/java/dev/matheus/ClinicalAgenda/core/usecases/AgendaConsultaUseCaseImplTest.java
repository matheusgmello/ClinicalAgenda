package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaDataInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaConsultaUseCaseImplTest {

    @Mock
    private ConsultaGateway consultaGateway;

    @InjectMocks
    private AgendaConsultaUseCaseImpl agendaConsultaUseCase;

    private Consulta consultaValida;

    @BeforeEach
    void setUp() {
        consultaValida = new Consulta(
                null,
                "João Silva",
                "Dor de cabeça",
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(1),
                "Consultório 01",
                "CRM123",
                null,
                TipoConsulta.PRESENCIAL
        );
    }

    @Test
    @DisplayName("Deve agendar uma consulta com sucesso")
    void deveAgendarConsultaComSucesso() {
        when(consultaGateway.contarConsultasPorPrefixo(anyString())).thenReturn(0L);
        when(consultaGateway.agendarConsulta(any(Consulta.class))).thenReturn(consultaValida);

        Consulta resultado = agendaConsultaUseCase.execute(consultaValida);

        assertNotNull(resultado);
        verify(consultaGateway, times(1)).agendarConsulta(any(Consulta.class));
        verify(consultaGateway, times(1)).contarConsultasPorPrefixo(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção ao agendar consulta para data passada")
    void deveLancarExcecaoParaDataPassada() {
        Consulta consultaPassada = new Consulta(
                null, "João", "Sintoma", null,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1),
                "01", "CRM", null, TipoConsulta.PRESENCIAL
        );

        assertThrows(ConsultaDataInvalidaException.class, () -> agendaConsultaUseCase.execute(consultaPassada));
        verify(consultaGateway, never()).agendarConsulta(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando data fim for anterior à data início")
    void deveLancarExcecaoQuandoDataFimForAnteriorADataInicio() {
        Consulta consultaInvalida = new Consulta(
                null, "João", "Sintoma", null,
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).minusHours(1),
                "01", "CRM", null, TipoConsulta.PRESENCIAL
        );

        assertThrows(ConsultaDataInvalidaException.class, () -> agendaConsultaUseCase.execute(consultaInvalida));
        verify(consultaGateway, never()).agendarConsulta(any());
    }
}
