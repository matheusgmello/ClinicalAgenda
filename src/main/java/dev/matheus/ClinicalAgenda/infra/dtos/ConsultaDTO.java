package dev.matheus.ClinicalAgenda.infra.dtos;

import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dados para criação ou atualização de uma consulta")
public record ConsultaDTO(
        @Schema(description = "Identificador único interno (gerado pelo banco)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,
        
        @Schema(description = "Nome completo do paciente", example = "João da Silva")
        String pacienteNome,
        
        @Schema(description = "Descrição dos sintomas relatados", example = "Dor de cabeça persistente e febre leve")
        String descricaoSintomas,
        
        @Schema(description = "Código identificador da consulta (gerado automaticamente no agendamento)", example = "CONS-2026-001")
        String identificador,
        
        @Schema(description = "Data e hora de início da consulta", example = "2026-03-25T14:30:00")
        LocalDateTime dataInicio,
        
        @Schema(description = "Data e hora de término da consulta", example = "2026-03-25T15:30:00")
        LocalDateTime dataFim,
        
        @Schema(description = "Identificação do consultório", example = "Sala 102 - Bloco A")
        String consultorio,
        
        @Schema(description = "CRM do médico responsável", example = "123456-SP")
        String crmMedico,
        
        @Schema(description = "URL da imagem da receita médica (se houver)", example = "https://clinica.com/receitas/cons-2026-001.png")
        String imgReceitaUrl,
        
        @Schema(description = "Tipo de atendimento da consulta", example = "PRESENCIAL")
        TipoConsulta tipo
) {
}