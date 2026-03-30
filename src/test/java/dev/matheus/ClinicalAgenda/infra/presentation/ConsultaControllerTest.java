package dev.matheus.ClinicalAgenda.infra.presentation;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;
import dev.matheus.ClinicalAgenda.core.usecases.*;
import dev.matheus.ClinicalAgenda.infra.dtos.ConsultaDTO;
import dev.matheus.ClinicalAgenda.infra.mapper.ConsultaDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaControllerTest {

    @Mock
    private AgendaConsultaUseCase agendarConsultaUseCase;
    @Mock
    private ListarConsultasUseCase listarConsultasUseCase;
    @Mock
    private BuscarConsultaPorIdentificadorUseCase buscarConsultaUseCase;
    @Mock
    private CancelarConsultaUseCase cancelarConsultaUseCase;
    @Mock
    private AlterarConsultaUseCase alterarConsultaUseCase;
    @Mock
    private ConsultaDTOMapper consultaDTOMapper;

    @InjectMocks
    private ConsultaController consultaController;

    private Consulta consulta;
    private ConsultaDTO consultaDTO;

    @BeforeEach
    void setUp() {
        consulta = new Consulta(1L, "João", "Sintoma", "CONS-2026-001", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "01", "CRM", null, TipoConsulta.PRESENCIAL);
        consultaDTO = new ConsultaDTO(1L, "João", "Sintoma", "CONS-2026-001", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "01", "CRM", null, TipoConsulta.PRESENCIAL);
    }

    @Test
    @DisplayName("Deve agendar consulta com sucesso e retornar 201 Created")
    void deveAgendarConsulta() {
        when(consultaDTOMapper.toDomain(any())).thenReturn(consulta);
        when(agendarConsultaUseCase.execute(any())).thenReturn(consulta);
        when(consultaDTOMapper.toDTO(any())).thenReturn(consultaDTO);

        ResponseEntity<ConsultaDTO> resposta = consultaController.agendar(consultaDTO);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals("CONS-2026-001", resposta.getBody().identificador());
    }

    @Test
    @DisplayName("Deve cancelar consulta e retornar 204 No Content")
    void deveCancelarConsulta() {
        ResponseEntity<Void> resposta = consultaController.cancelar("CONS-2026-001");

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        verify(cancelarConsultaUseCase).execute("CONS-2026-001");
    }

    @Test
    @DisplayName("Deve buscar consulta por identificador")
    void deveBuscarPorIdentificador() {
        when(buscarConsultaUseCase.execute("CONS-2026-001")).thenReturn(consulta);
        when(consultaDTOMapper.toDTO(any())).thenReturn(consultaDTO);

        ResponseEntity<ConsultaDTO> resposta = consultaController.buscarPorIdentificador("CONS-2026-001");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals("João", resposta.getBody().pacienteNome());
    }
}
