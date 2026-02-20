package dev.matheus.ClinicalAgenda.infra.gateway;

import dev.matheus.ClinicalAgenda.core.entities.Consulta;
import dev.matheus.ClinicalAgenda.core.gateway.ConsultaGateway;
import dev.matheus.ClinicalAgenda.infra.mapper.ConsultaEntityMapper;
import dev.matheus.ClinicalAgenda.infra.persistence.ConsultaEntity;
import dev.matheus.ClinicalAgenda.infra.persistence.ConsultaRepository;
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
    public void cancelarConsulta(String identificador) {
        consultaRepository.deleteByIdentificador(identificador);
    }
}
