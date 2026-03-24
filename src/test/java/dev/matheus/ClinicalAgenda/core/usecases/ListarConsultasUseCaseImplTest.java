package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarConsultasUseCaseImplTest {

    @Mock
    private ConsultaGateway consultaGateway;

    @InjectMocks
    private ListarConsultasUseCaseImpl listarConsultasUseCase;

    @Test
    @DisplayName("Deve listar todas as consultas")
    void deveListarTodasConsultas() {
        when(consultaGateway.listarTodasConsultas()).thenReturn(List.of(mock(Consulta.class)));

        List<Consulta> resultado = listarConsultasUseCase.execute();

        assertEquals(1, resultado.size());
        verify(consultaGateway, times(1)).listarTodasConsultas();
    }
}
