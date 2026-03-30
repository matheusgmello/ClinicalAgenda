package dev.matheus.ClinicalAgenda.infra.presentation;

import dev.matheus.ClinicalAgenda.core.enums.UserRole;
import dev.matheus.ClinicalAgenda.infra.persistence.UsuarioEntity;
import dev.matheus.ClinicalAgenda.infra.persistence.UsuarioRepository;
import dev.matheus.ClinicalAgenda.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AutenticacaoController autenticacaoController;

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar token")
    void deveRealizarLogin() {
        var authDTO = new AutenticacaoController.AuthenticationDTO("user", "password");
        var usuario = new UsuarioEntity(1L, "user", "encrypted", UserRole.PACIENTE);
        var auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(usuario);
        when(tokenService.generateToken(usuario)).thenReturn("token-123");

        ResponseEntity resposta = autenticacaoController.login(authDTO);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        AutenticacaoController.LoginResponseDTO body = (AutenticacaoController.LoginResponseDTO) resposta.getBody();
        assertEquals("token-123", body.token());
    }

    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void deveRegistrarUsuario() {
        var registerDTO = new AutenticacaoController.RegisterDTO("newuser", "password", UserRole.MEDICO);
        when(repository.findByLogin("newuser")).thenReturn(null);

        ResponseEntity resposta = autenticacaoController.register(registerDTO);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Não deve registrar usuário com login já existente")
    void naoDeveRegistrarUsuarioExistente() {
        var registerDTO = new AutenticacaoController.RegisterDTO("user", "password", UserRole.PACIENTE);
        when(repository.findByLogin("user")).thenReturn(new UsuarioEntity());

        ResponseEntity resposta = autenticacaoController.register(registerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(repository, never()).save(any());
    }
}
