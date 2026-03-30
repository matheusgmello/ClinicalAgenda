package dev.matheus.ClinicalAgenda.infra.mapper;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.infra.persistence.ConsultaEntity;
import org.springframework.stereotype.Component;

@Component
public class ConsultaEntityMapper {

    public ConsultaEntity toEntity(Consulta consulta) {
        ConsultaEntity entity = new ConsultaEntity();
        entity.setId(consulta.id());
        entity.setPacienteNome(consulta.pacienteNome());
        entity.setDescricaoSintomas(consulta.descricaoSintomas());
        entity.setIdentificador(consulta.identificador());
        entity.setDataInicio(consulta.dataInicio());
        entity.setDataFim(consulta.dataFim());
        entity.setConsultorio(consulta.consultorio());
        entity.setCrmMedico(consulta.crmMedico());
        entity.setImgReceitaUrl(consulta.imgReceitaUrl());
        entity.setTipo(consulta.tipo());
        return entity;
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
