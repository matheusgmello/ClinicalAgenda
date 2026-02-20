package dev.matheus.ClinicalAgenda.infra.mapper;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.infra.dtos.ConsultaDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsultaDTOMapper {

    public ConsultaDTO toDTO(Consulta consulta) {
        return new ConsultaDTO(
                consulta.id(),
                consulta.pacienteNome(),
                consulta.descricaoSintomas(),
                consulta.identificador(),
                consulta.dataInicio(),
                consulta.dataFim(),
                consulta.consultorio(),
                consulta.crmMedico(),
                consulta.imgReceitaUrl(),
                consulta.tipo()

        );
    }

    public Consulta toDomain(ConsultaDTO consultaDTO) {
        return new Consulta(
                consultaDTO.id(),
                consultaDTO.pacienteNome(),
                consultaDTO.descricaoSintomas(),
                consultaDTO.identificador(),
                consultaDTO.dataInicio(),
                consultaDTO.dataFim(),
                consultaDTO.consultorio(),
                consultaDTO.crmMedico(),
                consultaDTO.imgReceitaUrl(),
                consultaDTO.tipo()

        );
    }
}
