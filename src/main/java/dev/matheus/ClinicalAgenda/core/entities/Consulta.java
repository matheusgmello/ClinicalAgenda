package dev.matheus.ClinicalAgenda.core.entities;

import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;

import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private String pacienteNome;
    private String descricaoSintomas;
    private String identificador;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String consultorio;
    private String crmMedico;
    private String imgReceitaUrl;
    private TipoConsulta tipo;
}