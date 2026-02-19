package dev.matheus.ClinicalAgenda.core.entities;

import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;

import java.time.LocalDateTime;

public record Consulta(
        Long id,
        String pacienteNome,
        String descricaoSintomas,
        String identificador,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String consultorio,
        String crmMedico,
        String imgReceitaUrl,
        TipoConsulta tipo) {
}