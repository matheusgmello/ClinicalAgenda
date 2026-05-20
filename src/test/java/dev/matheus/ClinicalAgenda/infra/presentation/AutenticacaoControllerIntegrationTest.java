package dev.matheus.ClinicalAgenda.infra.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheus.ClinicalAgenda.core.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AutenticacaoControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Deve registrar usuario e realizar login com sucesso")
    void deveRegistrarERealizarLogin() throws Exception {
        var register = new AutenticacaoController.RegisterDTO("integrationuser@test.com", "senha123", UserRole.PACIENTE);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        var login = new AutenticacaoController.AuthenticationDTO("integrationuser@test.com", "senha123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar registrar usuario duplicado")
    void deveBloquearRegistroDuplicado() throws Exception {
        var register = new AutenticacaoController.RegisterDTO("duplicado@test.com", "senha123", UserRole.PACIENTE);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 401 ao tentar login com credenciais incorretas")
    void deveBloquearLoginComCredenciaisErradas() throws Exception {
        var login = new AutenticacaoController.AuthenticationDTO("naoexiste@test.com", "senhaerrada");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}
