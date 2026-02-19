package dev.matheus.ClinicalAgenda.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Long> {

    Optional<ConsultaEntity> findByIdentificador(String identificador);

    boolean existsByIdentificador(String identificador);

    void deleteByIdentificador(String identificador);
}