package dev.matheus.ClinicalAgenda.infra.persistence;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<ConsultaEntity, Long> {

    Optional<ConsultaEntity> findByIdentificador(String identificador);

    boolean existsByIdentificador(String identificador);

    @Transactional
    @Modifying
    void deleteByIdentificador(String identificador);

    @Query("SELECT COUNT(c) FROM ConsultaEntity c WHERE c.identificador LIKE :prefixo%")
    long contarPorIdentificadorIniciandoCom(@Param("prefixo") String prefixo);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ConsultaEntity c " +
           "WHERE c.crmMedico = :crm " +
           "AND (:identificador IS NULL OR c.identificador <> :identificador) " +
           "AND ((c.dataInicio < :fim AND c.dataFim > :inicio))")
    boolean existeConflitoMedico(@Param("crm") String crm, 
                                @Param("inicio") LocalDateTime inicio, 
                                @Param("fim") LocalDateTime fim,
                                @Param("identificador") String identificador);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ConsultaEntity c " +
           "WHERE c.consultorio = :consultorio " +
           "AND (:identificador IS NULL OR c.identificador <> :identificador) " +
           "AND ((c.dataInicio < :fim AND c.dataFim > :inicio))")
    boolean existeConflitoConsultorio(@Param("consultorio") String consultorio, 
                                     @Param("inicio") LocalDateTime inicio, 
                                     @Param("fim") LocalDateTime fim,
                                     @Param("identificador") String identificador);
    }