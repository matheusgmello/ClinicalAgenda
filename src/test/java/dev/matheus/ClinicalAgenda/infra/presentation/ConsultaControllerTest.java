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
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
        consulta = new Consulta(1L, "João", "Sintoma", "CONS-2026-001", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "01", "CRM", null, TipoConsulta.PRESENCIAL);
        consultaDTO = new ConsultaDTO(1L, "João", "Sintoma", "CONS-2026-001", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "01", "CRM", null, TipoConsulta.PRESENCIAL);
    }

    @Test
    @DisplayName("Deve agendar consulta com sucesso")
    void deveAgendarConsulta() {
        when(consultaDTOMapper.toDomain(any())).thenReturn(consulta);
        when(agendarConsultaUseCase.execute(any())).thenReturn(consulta);
        when(consultaDTOMapper.toDTO(any())).thenReturn(consultaDTO);

        ResponseEntity<Map<String, Object>> resposta = consultaController.agendar(consultaDTO);

        assertEquals(200, resposta.getStatusCode().value());
        assertTrue(resposta.getBody().containsKey("Mensagem: "));
        verify(agendarConsultaUseCase).execute(any());
    }

    @Test
    @DisplayName("Deve alterar consulta com sucesso")
    void deveAlterarConsulta() {
        when(alterarConsultaUseCase.execute(any())).thenReturn(consulta);
        when(consultaDTOMapper.toDTO(any())).thenReturn(consultaDTO);

        ResponseEntity<Map<String, Object>> resposta = consultaController.alterar("CONS-2026-001", consultaDTO);

        assertEquals(200, resposta.getStatusCode().value());
        assertEquals("Consulta atualizada com sucesso", resposta.getBody().get("Mensagem: "));
        verify(alterarConsultaUseCase).execute(any());
    }

    @Test
    @DisplayName("Deve listar consultas")
    void deveListarConsultas() {
        when(listarConsultasUseCase.execute()).thenReturn(List.of(consulta));
        when(consultaDTOMapper.toDTO(any())).thenReturn(consultaDTO);

        List<ConsultaDTO> resultado = consultaController.buscarConsultas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Deve buscar por identificador")
    void deveBuscarPorIdentificador() {
        when(buscarConsultaUseCase.execute("CONS-2026-001")).thenReturn(consulta);

        ResponseEntity<Consulta> resposta = consultaController.buscarPorIdentificador("CONS-2026-001");

        assertEquals(200, resposta.getStatusCode().value());
        assertNotNull(resposta.getBody());
    }

    @Test
    @DisplayName("Deve cancelar consulta")
    void deveCancelarConsulta() {
        ResponseEntity<Map<String, Object>> resposta = consultaController.cancelar("CONS-2026-001");

        assertEquals(200, resposta.getStatusCode().value());
        verify(cancelarConsultaUseCase).execute("CONS-2026-001");
    }
}
