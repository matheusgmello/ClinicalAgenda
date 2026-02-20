package dev.matheus.ClinicalAgenda.infra.dtos;

import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;

import java.time.LocalDateTime;

public record ConsultaDTO(
        Long id,
        String pacienteNome,
        String descricaoSintomas,
        String identificador,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        String consultorio,
        String crmMedico,
        String imgReceitaUrl,
        TipoConsulta tipo
) {
}