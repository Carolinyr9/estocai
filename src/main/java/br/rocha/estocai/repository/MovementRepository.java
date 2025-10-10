package br.rocha.estocai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.rocha.estocai.model.Movement;

public interface MovementRepository extends JpaRepository<Movement, Long> {
}