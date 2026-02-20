package dev.matheus.ClinicalAgenda.infra.presentation;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.usecases.AgendaConsultaUseCase;
import dev.matheus.ClinicalAgenda.core.usecases.BuscarConsultaPorIdentificadorUseCase;
import dev.matheus.ClinicalAgenda.core.usecases.CancelarConsultaUseCase;
import dev.matheus.ClinicalAgenda.core.usecases.ListarConsultasUseCase;
import dev.matheus.ClinicalAgenda.infra.dtos.ConsultaDTO;
import dev.matheus.ClinicalAgenda.infra.mapper.ConsultaDTOMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/consultas")
public class ConsultaController {

    private final AgendaConsultaUseCase agendarConsultaUseCase;
    private final ListarConsultasUseCase listarConsultasUseCase;
    private final BuscarConsultaPorIdentificadorUseCase buscarConsultaUseCase;
    private final CancelarConsultaUseCase cancelarConsultaUseCase;
    private final ConsultaDTOMapper consultaDTOMapper;

    public ConsultaController(AgendaConsultaUseCase agendarConsultaUseCase,
                            ListarConsultasUseCase listarConsultasUseCase, BuscarConsultaPorIdentificadorUseCase buscarConsultaUseCase,
                            CancelarConsultaUseCase cancelarConsultaUseCase,
                            ConsultaDTOMapper consultaDTOMapper) {
        this.agendarConsultaUseCase = agendarConsultaUseCase;
        this.listarConsultasUseCase = listarConsultasUseCase;
        this.buscarConsultaUseCase = buscarConsultaUseCase;
        this.cancelarConsultaUseCase = cancelarConsultaUseCase;
        this.consultaDTOMapper = consultaDTOMapper;
    }

    @PostMapping("/agendar")
    public ResponseEntity<Map<String, Object>> agendar(@RequestBody ConsultaDTO consultaDto) {
        Consulta novaConsulta = agendarConsultaUseCase.execute(consultaDTOMapper.toDomain(consultaDto));

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("Mensagem: ", "A consulta foi agendada com sucesso no banco de dados");
        resposta.put("Dados da consulta: ", consultaDTOMapper.toDTO(novaConsulta));

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/listar")
    public List<ConsultaDTO> buscarConsultas() {
        return listarConsultasUseCase.execute()
                .stream()
                .map(consultaDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/listar/{identificador}")
    public ResponseEntity<Consulta> buscarPorIdentificador(@PathVariable String identificador) {
        Consulta consulta = buscarConsultaUseCase.execute(identificador);
        return ResponseEntity.ok(consulta);
    }

    @DeleteMapping("/cancelar/{identificador}")
    public ResponseEntity<Map<String, Object>> cancelar(@PathVariable String identificador) {
        cancelarConsultaUseCase.execute(identificador);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("Mensagem: ", "Consulta cancelada com sucesso");

        return ResponseEntity.ok(resposta);
    }




}
