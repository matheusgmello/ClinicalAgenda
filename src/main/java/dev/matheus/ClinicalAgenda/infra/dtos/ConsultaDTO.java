package dev.matheus.ClinicalAgenda.infra.dtos;

import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Dados para criação ou atualização de uma consulta")
public record ConsultaDTO(
        @Schema(description = "Identificador único interno (gerado pelo banco)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @NotBlank(message = "O nome do paciente é obrigatório")
        @Schema(description = "Nome completo do paciente", example = "João da Silva")
        String pacienteNome,

        @NotBlank(message = "A descrição dos sintomas é obrigatória")
        @Schema(description = "Descrição dos sintomas relatados", example = "Dor de cabeça persistente e febre leve")
        String descricaoSintomas,

        @Schema(description = "Código identificador da consulta (gerado automaticamente no agendamento)", example = "CONS-2026-001")
        String identificador,

        @NotNull(message = "A data de início é obrigatória")
        @FutureOrPresent(message = "A data de início não pode ser no passado")
        @Schema(description = "Data e hora de início da consulta", example = "2026-03-25T14:30:00")
        LocalDateTime dataInicio,

        @NotNull(message = "A data de término é obrigatória")
        @Schema(description = "Data e hora de término da consulta", example = "2026-03-25T15:30:00")
        LocalDateTime dataFim,

        @NotBlank(message = "O consultório é obrigatório")
        @Schema(description = "Identificação do consultório", example = "Sala 102 - Bloco A")
        String consultorio,

        @NotBlank(message = "O CRM do médico é obrigatório")
        @Schema(description = "CRM do médico responsável", example = "123456-SP")
        String crmMedico,

        @Schema(description = "URL da imagem da receita médica (se houver)", example = "https://clinica.com/receitas/cons-2026-001.png")
        String imgReceitaUrl,

        @NotNull(message = "O tipo de consulta é obrigatório")
        @Schema(description = "Tipo de atendimento da consulta", example = "PRESENCIAL")
        TipoConsulta tipo
) {
}