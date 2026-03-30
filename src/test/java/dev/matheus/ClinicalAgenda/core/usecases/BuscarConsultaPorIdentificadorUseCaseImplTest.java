package dev.matheus.ClinicalAgenda.core.usecases;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.core.exceptions.ConsultaNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarConsultaPorIdentificadorUseCaseImplTest {

    @Mock
    private ConsultaGateway consultaGateway;

    @InjectMocks
    private BuscarConsultaPorIdentificadorUseCaseImpl buscarConsultaUseCase;

    @Test
    @DisplayName("Deve buscar consulta por identificador com sucesso")
    void deveBuscarConsultaComSucesso() {
        Consulta consulta = mock(Consulta.class);
        when(consultaGateway.buscarConsultaPorIdentificador("ID123")).thenReturn(Optional.of(consulta));

        Consulta resultado = buscarConsultaUseCase.execute("ID123");

        assertNotNull(resultado);
        verify(consultaGateway, times(1)).buscarConsultaPorIdentificador("ID123");
    }

    @Test
    @DisplayName("Deve lançar exceção quando consulta não for encontrada")
    void deveLancarExcecaoQuandoNaoEncontrada() {
        when(consultaGateway.buscarConsultaPorIdentificador("ID123")).thenReturn(Optional.empty());

        assertThrows(ConsultaNotFoundException.class, () -> buscarConsultaUseCase.execute("ID123"));
    }
}
