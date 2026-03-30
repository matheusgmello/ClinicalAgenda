package dev.matheus.ClinicalAgenda.core.entities;

import dev.matheus.ClinicalAgenda.core.enums.UserRole;

public record Usuario(
        Long id,
        String login,
        String senha,
        UserRole role
) {
}
