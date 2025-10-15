package br.rocha.estocai.repository;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;

import br.rocha.estocai.model.Movement;
import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    Page<Movement> findByProductId(Long productId, Pageable pageable);

    Page<Movement> findByType(MovementType type, Pageable pageable);

    Page<Movement> findByDescription(MovementDescription description, Pageable pageable);

    @Query(value = """
        SELECT *
        FROM movements
        WHERE (:startDate IS NULL OR date >= :startDate)
        AND (:endDate IS NULL OR date <= :endDate)
    """, nativeQuery = true)
    Page<Movement> findBetweenDate(
            @Param("startDate") java.sql.Timestamp startDate,
            @Param("endDate") java.sql.Timestamp endDate,
            Pageable pageable);


}