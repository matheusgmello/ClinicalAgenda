package dev.matheus.ClinicalAgenda.infra.mapper;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.infra.persistence.ConsultaEntity;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEntityMapper {

    public ConsultaEntity toEntity(Consulta consulta) {
        return new ConsultaEntity(
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

    public Consulta toDomain(ConsultaEntity consultaEntity) {
        return new Consulta(
                consultaEntity.getId(),
                consultaEntity.getPacienteNome(),
                consultaEntity.getDescricaoSintomas(),
                consultaEntity.getIdentificador(),
                consultaEntity.getDataInicio(),
                consultaEntity.getDataFim(),
                consultaEntity.getConsultorio(),
                consultaEntity.getCrmMedico(),
                consultaEntity.getImgReceitaUrl(),
                consultaEntity.getTipo()
        );
    }

}
