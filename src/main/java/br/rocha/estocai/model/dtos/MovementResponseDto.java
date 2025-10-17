package br.rocha.estocai.model.dtos;

import java.util.Date;

import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;

public record MovementResponseDto(Long id, Product product, Date date, MovementType type, MovementDescription description, UserResponseDto user) {
    
}
