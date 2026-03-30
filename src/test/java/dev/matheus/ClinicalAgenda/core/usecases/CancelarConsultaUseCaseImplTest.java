package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.core.exceptions.ConsultaNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelarConsultaUseCaseImplTest {

    @Mock
    private ConsultaGateway consultaGateway;

    @InjectMocks
    private CancelarConsultaUseCaseImpl cancelarConsultaUseCase;

    @Test
    @DisplayName("Deve cancelar consulta com sucesso")
    void deveCancelarConsultaComSucesso() {
        when(consultaGateway.existePorIdentificador("ID123")).thenReturn(true);

        cancelarConsultaUseCase.execute("ID123");

        verify(consultaGateway, times(1)).cancelarConsulta("ID123");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar consulta inexistente")
    void deveLancarExcecaoQuandoInexistente() {
        when(consultaGateway.existePorIdentificador("ID123")).thenReturn(false);

        assertThrows(ConsultaNotFoundException.class, () -> cancelarConsultaUseCase.execute("ID123"));
        verify(consultaGateway, never()).cancelarConsulta(anyString());
    }
}
