package dev.matheus.ClinicalAgenda.infra.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;
import dev.matheus.ClinicalAgenda.infra.dtos.ConsultaDTO;
import dev.matheus.ClinicalAgenda.infra.notifications.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ConsultaControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @MockitoBean
    private EmailService emailService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private ConsultaDTO consultaValida() {
        return new ConsultaDTO(
                null,
                "Ana Lima",
                "ana@email.com",
                "Dor de cabeça frequente",
                null,
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(3).plusHours(1),
                "Sala 101",
                "CRM-IT-001",
                null,
                TipoConsulta.PRESENCIAL
        );
    }

    @Test
    @DisplayName("Deve agendar consulta e retornar 201 com ROLE_PACIENTE")
    @WithMockUser(roles = "PACIENTE")
    void deveAgendarConsulta() throws Exception {
        mockMvc.perform(post("/api/v1/consultas/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consultaValida())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pacienteNome").value("Ana Lima"))
                .andExpect(jsonPath("$.identificador").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar 403 ao agendar sem ROLE_PACIENTE")
    @WithMockUser(roles = "MEDICO")
    void deveBloquearAgendamentoSemPermissao() throws Exception {
        mockMvc.perform(post("/api/v1/consultas/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consultaValida())))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 401 ao agendar sem autenticacao")
    void deveRetornar401SemAutenticacao() throws Exception {
        mockMvc.perform(post("/api/v1/consultas/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(consultaValida())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 400 ao agendar com dados invalidos")
    @WithMockUser(roles = "PACIENTE")
    void deveRetornar400ComDadosInvalidos() throws Exception {
        ConsultaDTO invalida = new ConsultaDTO(null, "", "", null, null, null, null, "", "", null, null);
        mockMvc.perform(post("/api/v1/consultas/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("CLINICAL-400"));
    }

    @Test
    @DisplayName("Deve listar consultas paginadas com ROLE_MEDICO")
    @WithMockUser(roles = "MEDICO")
    void deveListarConsultasPaginadas() throws Exception {
        mockMvc.perform(get("/api/v1/consultas/listar")
                        .param("pagina", "0")
                        .param("tamanho", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paginaAtual").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10))
                .andExpect(jsonPath("$.conteudo").isArray());
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar consulta inexistente")
    @WithMockUser(roles = "PACIENTE")
    void deveRetornar404ParaConsultaInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/consultas/listar/CONS-0000-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("CLINICAL-001"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao cancelar consulta inexistente com ROLE_ADMIN")
    @WithMockUser(roles = "ADMIN")
    void deveRetornar404AoCancelarInexistente() throws Exception {
        mockMvc.perform(delete("/api/v1/consultas/cancelar/CONS-0000-999"))
                .andExpect(status().isNotFound());
    }
}
