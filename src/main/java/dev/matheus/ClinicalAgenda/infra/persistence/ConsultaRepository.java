package dev.matheus.ClinicalAgenda.infra.persistence;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Long> {

    Optional<ConsultaEntity> findByIdentificador(String identificador);

    boolean existsByIdentificador(String identificador);

    @Transactional
    @Modifying
    void deleteByIdentificador(String identificador);
}