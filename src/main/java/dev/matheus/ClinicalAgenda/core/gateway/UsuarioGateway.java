package dev.matheus.ClinicalAgenda.core.gateway;

import dev.matheus.ClinicalAgenda.core.entities.Usuario;
import java.util.Optional;

public interface UsuarioGateway {
    Optional<Usuario> buscarPorLogin(String login);
    Usuario salvar(Usuario usuario);
}
