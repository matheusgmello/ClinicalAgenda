package dev.matheus.ClinicalAgenda.infra.presentation;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.usecases.*;
import dev.matheus.ClinicalAgenda.infra.dtos.ConsultaDTO;
import dev.matheus.ClinicalAgenda.infra.mapper.ConsultaDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/consultas")
@Tag(name = "Consultas", description = "Operações para agendamento, consulta, alteração e cancelamento de consultas")
public class ConsultaController {

    private final AgendaConsultaUseCase agendarConsultaUseCase;
    private final ListarConsultasUseCase listarConsultasUseCase;
    private final BuscarConsultaPorIdentificadorUseCase buscarConsultaUseCase;
    private final CancelarConsultaUseCase cancelarConsultaUseCase;
    private final AlterarConsultaUseCase alterarConsultaUseCase;
    private final ConsultaDTOMapper consultaDTOMapper;

    public ConsultaController(AgendaConsultaUseCase agendarConsultaUseCase,
                            ListarConsultasUseCase listarConsultasUseCase, BuscarConsultaPorIdentificadorUseCase buscarConsultaUseCase,
                            CancelarConsultaUseCase cancelarConsultaUseCase,
                            AlterarConsultaUseCase alterarConsultaUseCase,
                            ConsultaDTOMapper consultaDTOMapper) {
        this.agendarConsultaUseCase = agendarConsultaUseCase;
        this.listarConsultasUseCase = listarConsultasUseCase;
        this.buscarConsultaUseCase = buscarConsultaUseCase;
        this.cancelarConsultaUseCase = cancelarConsultaUseCase;
        this.alterarConsultaUseCase = alterarConsultaUseCase;
        this.consultaDTOMapper = consultaDTOMapper;
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar uma nova consulta", description = "Cria um novo agendamento e gera automaticamente o identificador único.")
    public ResponseEntity<Map<String, Object>> agendar(@RequestBody ConsultaDTO consultaDto) {
        Consulta novaConsulta = agendarConsultaUseCase.execute(consultaDTOMapper.toDomain(consultaDto));

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("Mensagem: ", "A consulta foi agendada com sucesso no banco de dados");
        resposta.put("Dados da consulta: ", consultaDTOMapper.toDTO(novaConsulta));

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/alterar/{identificador}")
    @Operation(summary = "Alterar dados de uma consulta", description = "Atualiza informações como consultório ou descrição de sintomas de uma consulta existente.")
    public ResponseEntity<Map<String, Object>> alterar(@PathVariable String identificador, @RequestBody ConsultaDTO consultaDto) {
        Consulta consultaParaAlterar = new Consulta(
                null,
                consultaDto.pacienteNome(),
                consultaDto.descricaoSintomas(),
                identificador,
                consultaDto.dataInicio(),
                consultaDto.dataFim(),
                consultaDto.consultorio(),
                consultaDto.crmMedico(),
                consultaDto.imgReceitaUrl(),
                consultaDto.tipo()
        );

        Consulta consultaAtualizada = alterarConsultaUseCase.execute(consultaParaAlterar);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("Mensagem: ", "Consulta atualizada com sucesso");
        resposta.put("Dados da consulta: ", consultaDTOMapper.toDTO(consultaAtualizada));

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar todas as consultas", description = "Retorna uma lista com todas as consultas agendadas.")
    public List<ConsultaDTO> buscarConsultas() {
        return listarConsultasUseCase.execute()
                .stream()
                .map(consultaDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/listar/{identificador}")
    @Operation(summary = "Buscar consulta por identificador", description = "Retorna os detalhes de uma consulta específica através do seu código único.")
    public ResponseEntity<Consulta> buscarPorIdentificador(@PathVariable String identificador) {
        Consulta consulta = buscarConsultaUseCase.execute(identificador);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/cancelar/{identificador}")
    @Operation(summary = "Cancelar uma consulta", description = "Remove o agendamento de uma consulta através do seu código identificador.")
    public ResponseEntity<Map<String, Object>> cancelar(@PathVariable String identificador) {
        cancelarConsultaUseCase.execute(identificador);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("Mensagem: ", "Consulta cancelada com sucesso");

        return ResponseEntity.ok(resposta);
    }




}
