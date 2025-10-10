package br.rocha.estocai.mappers;

import org.mapstruct.Mapper;

import br.rocha.estocai.model.Movement;
import br.rocha.estocai.model.dtos.MovementResponseDto;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    MovementResponseDto movementToMovementResponseDto(Movement movement);
    
}
