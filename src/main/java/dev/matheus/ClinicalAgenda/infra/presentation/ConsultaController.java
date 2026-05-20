package dev.matheus.ClinicalAgenda.infra.presentation;

import dev.matheus.ClinicalAgenda.core.dtos.PaginaResponse;
import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.usecases.*;
import dev.matheus.ClinicalAgenda.infra.dtos.ConsultaDTO;
import dev.matheus.ClinicalAgenda.infra.mapper.ConsultaDTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/consultas")
@Tag(name = "Consultas", description = "Operações para agendamento, consulta, alteração e cancelamento de consultas")
public class ConsultaController {

    private final AgendaConsultaUseCase agendarConsultaUseCase;
    private final ListarConsultasPaginadasUseCase listarConsultasPaginadasUseCase;
    private final BuscarConsultaPorIdentificadorUseCase buscarConsultaUseCase;
    private final CancelarConsultaUseCase cancelarConsultaUseCase;
    private final AlterarConsultaUseCase alterarConsultaUseCase;
    private final ConsultaDTOMapper consultaDTOMapper;

    public ConsultaController(AgendaConsultaUseCase agendarConsultaUseCase,
                              ListarConsultasPaginadasUseCase listarConsultasPaginadasUseCase,
                              BuscarConsultaPorIdentificadorUseCase buscarConsultaUseCase,
                              CancelarConsultaUseCase cancelarConsultaUseCase,
                              AlterarConsultaUseCase alterarConsultaUseCase,
                              ConsultaDTOMapper consultaDTOMapper) {
        this.agendarConsultaUseCase = agendarConsultaUseCase;
        this.listarConsultasPaginadasUseCase = listarConsultasPaginadasUseCase;
        this.buscarConsultaUseCase = buscarConsultaUseCase;
        this.cancelarConsultaUseCase = cancelarConsultaUseCase;
        this.alterarConsultaUseCase = alterarConsultaUseCase;
        this.consultaDTOMapper = consultaDTOMapper;
    }

    @PostMapping("/agendar")
    @Operation(summary = "Agendar uma nova consulta", description = "Cria um novo agendamento e gera automaticamente o identificador único.")
    public ResponseEntity<ConsultaDTO> agendar(@Valid @RequestBody ConsultaDTO consultaDto) {
        Consulta novaConsulta = agendarConsultaUseCase.execute(consultaDTOMapper.toDomain(consultaDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaDTOMapper.toDTO(novaConsulta));
    }

    @PutMapping("/alterar/{identificador}")
    @Operation(summary = "Alterar dados de uma consulta", description = "Atualiza informações de uma consulta existente.")
    public ResponseEntity<ConsultaDTO> alterar(@PathVariable String identificador, @Valid @RequestBody ConsultaDTO consultaDto) {
        Consulta consultaParaAlterar = new Consulta(
                null,
                consultaDto.pacienteNome(),
                consultaDto.pacienteEmail(),
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
        return ResponseEntity.ok(consultaDTOMapper.toDTO(consultaAtualizada));
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar consultas paginadas", description = "Retorna uma página de consultas, ordenadas por data de início decrescente.")
    public ResponseEntity<PaginaResponse<ConsultaDTO>> buscarConsultas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        PaginaResponse<Consulta> resultado = listarConsultasPaginadasUseCase.execute(pagina, tamanho);
        List<ConsultaDTO> dtos = resultado.conteudo().stream().map(consultaDTOMapper::toDTO).toList();
        return ResponseEntity.ok(new PaginaResponse<>(dtos, resultado.paginaAtual(), resultado.tamanhoPagina(), resultado.totalElementos(), resultado.totalPaginas()));
    }

    @GetMapping("/listar/{identificador}")
    @Operation(summary = "Buscar consulta por identificador", description = "Retorna os detalhes de uma consulta específica.")
    public ResponseEntity<ConsultaDTO> buscarPorIdentificador(@PathVariable String identificador) {
        Consulta consulta = buscarConsultaUseCase.execute(identificador);
        return ResponseEntity.ok(consultaDTOMapper.toDTO(consulta));
    }

    @DeleteMapping("/cancelar/{identificador}")
    @Operation(summary = "Cancelar uma consulta", description = "Remove o agendamento de uma consulta através do seu código identificador.")
    public ResponseEntity<Void> cancelar(@PathVariable String identificador) {
        cancelarConsultaUseCase.execute(identificador);
        return ResponseEntity.noContent().build();
    }
}
