package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaDataInvalidaException;
import dev.matheus.ClinicalAgenda.infra.exceptions.ConsultaNotFoundException;
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
class AlterarConsultaUseCaseImplTest {

    @Mock
    private ConsultaGateway consultaGateway;

    @InjectMocks
    private AlterarConsultaUseCaseImpl alterarConsultaUseCase;

    private Consulta consultaExistente;

    @BeforeEach
    void setUp() {
        consultaExistente = new Consulta(
                1L,
                "Maria Souza",
                "Dor nas costas",
                "CONS-2026-001",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(2).plusHours(1),
                "Consultório 02",
                "CRM456",
                null,
                TipoConsulta.TELECONSULTA
        );
    }

    @Test
    @DisplayName("Deve alterar uma consulta com sucesso")
    void deveAlterarConsultaComSucesso() {
        when(consultaGateway.existePorIdentificador("CONS-2026-001")).thenReturn(true);
        when(consultaGateway.atualizarConsulta(any(Consulta.class))).thenReturn(consultaExistente);

        Consulta resultado = alterarConsultaUseCase.execute(consultaExistente);

        assertNotNull(resultado);
        assertEquals("CONS-2026-001", resultado.identificador());
        verify(consultaGateway, times(1)).atualizarConsulta(any(Consulta.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar consulta inexistente")
    void deveLancarExcecaoParaConsultaInexistente() {
        when(consultaGateway.existePorIdentificador(anyString())).thenReturn(false);

        assertThrows(ConsultaNotFoundException.class, () -> alterarConsultaUseCase.execute(consultaExistente));
        verify(consultaGateway, never()).atualizarConsulta(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar consulta para data passada")
    void deveLancarExcecaoParaDataPassada() {
        when(consultaGateway.existePorIdentificador(anyString())).thenReturn(true);
        Consulta consultaPassada = new Consulta(
                1L, "Maria", "Sintoma", "CONS-2026-001",
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1),
                "01", "CRM", null, TipoConsulta.TELECONSULTA
        );

        assertThrows(ConsultaDataInvalidaException.class, () -> alterarConsultaUseCase.execute(consultaPassada));
        verify(consultaGateway, never()).atualizarConsulta(any());
    }
}
