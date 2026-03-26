package dev.matheus.ClinicalAgenda.infra.gateway;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.mapper.ConsultaEntityMapper;
import dev.matheus.ClinicalAgenda.infra.persistence.ConsultaEntity;
import dev.matheus.ClinicalAgenda.infra.persistence.ConsultaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConsultaRepositoryGateway implements ConsultaGateway {

    private final ConsultaRepository consultaRepository;
    private final ConsultaEntityMapper consultaEntityMapper;

    public ConsultaRepositoryGateway(ConsultaRepository consultaRepository, ConsultaEntityMapper consultaEntityMapper) {
        this.consultaRepository = consultaRepository;
        this.consultaEntityMapper = consultaEntityMapper;
    }

    @Override
    public Consulta agendarConsulta(Consulta consulta) {
        ConsultaEntity entity = consultaEntityMapper.toEntity(consulta);
        ConsultaEntity novaConsulta = consultaRepository.save(entity);
        return consultaEntityMapper.toDomain(novaConsulta);
    }

    @Override
    public List<Consulta> listarTodasConsultas() {
        return consultaRepository.findAll().stream().map(consultaEntityMapper::toDomain).toList();
    }

    @Override
    public boolean existePorIdentificador(String identificador) {
        return consultaRepository.existsByIdentificador(identificador);
    }

    @Override
    public Optional<Consulta> buscarConsultaPorIdentificador(String identificador) {
        return consultaRepository.findByIdentificador(identificador)
                .map(consultaEntityMapper::toDomain);
    }


    @Override
    @Transactional
    public void cancelarConsulta(String identificador) {
        consultaRepository.deleteByIdentificador(identificador);
    }

    @Override
    public long contarConsultasPorPrefixo(String prefixo) {
        return consultaRepository.contarPorIdentificadorIniciandoCom(prefixo);
    }

    @Override
    @Transactional
    public Consulta atualizarConsulta(Consulta consulta) {
        ConsultaEntity entity = consultaRepository.findByIdentificador(consulta.identificador())
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada com o identificador: " + consulta.identificador()));
        
        entity.setPacienteNome(consulta.pacienteNome());
        entity.setDescricaoSintomas(consulta.descricaoSintomas());
        entity.setDataInicio(consulta.dataInicio());
        entity.setDataFim(consulta.dataFim());
        entity.setConsultorio(consulta.consultorio());
        entity.setCrmMedico(consulta.crmMedico());
        entity.setImgReceitaUrl(consulta.imgReceitaUrl());
        entity.setTipo(consulta.tipo());

        ConsultaEntity atualizada = consultaRepository.save(entity);
        return consultaEntityMapper.toDomain(atualizada);
    }

    @Override
    public boolean existeConflitoMedico(String crmMedico, java.time.LocalDateTime inicio, java.time.LocalDateTime fim, String identificadorExcluido) {
        return consultaRepository.existeConflitoMedico(crmMedico, inicio, fim, identificadorExcluido);
    }

    @Override
    public boolean existeConflitoConsultorio(String consultorio, java.time.LocalDateTime inicio, java.time.LocalDateTime fim, String identificadorExcluido) {
        return consultaRepository.existeConflitoConsultorio(consultorio, inicio, fim, identificadorExcluido);
    }
}
