package br.rocha.estocai.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import br.rocha.estocai.model.Movement;
import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    Page<Movement> findByProductId(Long productId, Pageable pageable);

    Page<Movement> findByType(MovementType type, Pageable pageable);

    Page<Movement> findByDescription(MovementDescription description, Pageable pageable);
}