package dev.matheus.ClinicalAgenda.infra.persistence;

import dev.matheus.ClinicalAgenda.core.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Consultas")
public class ConsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_nome", nullable = false)
    private String pacienteNome;

    @Column(name = "descricao_sintomas")
    private String descricaoSintomas;

    @Column(nullable = false, unique = true)
    private String identificador;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(nullable = false)
    private String consultorio;

    @Column(name = "crm_medico", nullable = false)
    private String crmMedico;

    @Column(name = "img_receita_url")
    private String imgReceitaUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoConsulta tipo;

}